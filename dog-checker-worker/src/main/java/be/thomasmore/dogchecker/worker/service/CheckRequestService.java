package be.thomasmore.dogchecker.worker.service;

import be.thomasmore.dogchecker.worker.dto.DogApiResponseDTO;
import be.thomasmore.dogchecker.worker.dto.WeatherApiResponseDTO;
import be.thomasmore.dogchecker.worker.dto.WorkerRequestDTO;
import be.thomasmore.dogchecker.worker.dto.WorkerResponseDTO;
import be.thomasmore.dogchecker.worker.service.SuitabilityService.SuitabilityResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import java.util.List;

@ApplicationScoped
public class CheckRequestService {

    private static final Logger LOG = Logger.getLogger(CheckRequestService.class);

    @Inject
    @RestClient
    DogApiClient dogApiClient;

    @Inject
    @RestClient
    WeatherApiClient weatherApiClient;

    @Inject
    SuitabilityService suitabilityService;

    @ConfigProperty(name = "dogs.api.key")
    String dogsApiKey;

    @ConfigProperty(name = "weather.api.key")
    String weatherApiKey;

    @Channel("check-response")
    Emitter<WorkerResponseDTO> emitter;

    @Incoming("check-request")
    public void processCheckRequest(WorkerRequestDTO request) {
        LOG.infof("Received check request: id=%d, breed=%s, city=%s",
                request.requestId(), request.breed(), request.city());

        try {
            // Call Dogs API
            // Note: The API always returns a JSON array, so we just take the first match
            List<DogApiResponseDTO> dogs = dogApiClient.getBreed(request.breed(), dogsApiKey);
            if (dogs == null || dogs.isEmpty()) {
                emitter.send(new WorkerResponseDTO(request.requestId(), "FAILED",
                        "Dog breed '" + request.breed() + "' not found."));
                return;
            }
            DogApiResponseDTO dog = dogs.get(0);

            // Call Weather API
            WeatherApiResponseDTO weather = weatherApiClient.getWeather(weatherApiKey, request.city());
            if (weather == null || weather.current == null) {
                emitter.send(new WorkerResponseDTO(request.requestId(), "FAILED",
                        "City '" + request.city() + "' not found."));
                return;
            }

            // Evaluate suitability
            SuitabilityResult result = suitabilityService.evaluate(
                    dog.coatLength, dog.energy, weather.current.tempC);
        
            emitter.send(new WorkerResponseDTO(request.requestId(), result.suitability(), result.reason()));

        } catch (Exception e) {
            LOG.errorf("Error processing request %d: %s", request.requestId(), e.getMessage());
            emitter.send(new WorkerResponseDTO(request.requestId(), "FAILED",
                    "An error occurred while processing the request."));
        }
    }
}
