package be.thomasmore.dogchecker.worker.service;

import be.thomasmore.dogchecker.worker.dto.DogApiResponseDTO;
import be.thomasmore.dogchecker.worker.dto.DogBreedInfo;
import be.thomasmore.dogchecker.worker.dto.WeatherApiResponseDTO;
import be.thomasmore.dogchecker.worker.dto.WorkerRequestDTO;
import be.thomasmore.dogchecker.worker.dto.WorkerResponseDTO;
import be.thomasmore.dogchecker.worker.service.SuitabilityService.SuitabilityResult;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import java.util.List;

@ApplicationScoped
public class CheckRequestService {

    private static final Logger LOG = Logger.getLogger(CheckRequestService.class);

    @Inject
    MeterRegistry registry;

    @Inject
    DogApiService dogApiService;

    @Inject
    WeatherApiService weatherApiService;

    @Inject
    SuitabilityService suitabilityService;

    @Channel("check-response")
    Emitter<WorkerResponseDTO> emitter;

    @Incoming("check-request")
    public void processCheckRequest(WorkerRequestDTO request) {
        LOG.infof("Received check request: id=%d, breed=%s, city=%s",
                request.requestId(), request.breed(), request.city());

        try {
            // Call Dogs API (with retry + fallback)
            List<DogApiResponseDTO> dogs = dogApiService.fetchBreed(request.breed());
            if (dogs == null || dogs.isEmpty()) {
                emitter.send(failedResponse(request.requestId(),
                        "Dog breed '" + request.breed() + "' not found."));
                return;
            }
            DogApiResponseDTO dog = dogs.get(0);

            // Call Weather API (with retry + fallback)
            WeatherApiResponseDTO weather = weatherApiService.fetchWeather(request.city());
            if (weather == null || weather.current == null) {
                emitter.send(failedResponse(request.requestId(),
                        "City '" + request.city() + "' not found."));
                return;
            }

            // Evaluate suitability
            SuitabilityResult result = suitabilityService.evaluate(
                    dog.coatLength, dog.energy, dog.maxWeightMale,
                    weather.current.tempC, weather.current.humidity);

            registry.counter("dog.checks.processed", "suitability", result.suitability()).increment();

            String matchedCity = weather.location.name + ", " + weather.location.country;
            
            DogBreedInfo breedInfo = new DogBreedInfo(
                    dog.imageLink,
                    dog.goodWithChildren, dog.goodWithOtherDogs, dog.goodWithStrangers,
                    dog.shedding, dog.grooming, dog.drooling, dog.coatLength,
                    dog.playfulness, dog.protectiveness, dog.trainability,
                    dog.energy, dog.barking);

            emitter.send(new WorkerResponseDTO(
                    request.requestId(), result.suitability(), result.reason(),
                    dog.name, matchedCity,
                    weather.current.tempC, weather.current.humidity,
                    breedInfo));

        } catch (Exception e) {
            LOG.errorf("Error processing request %d: %s", request.requestId(), e.getMessage());
            emitter.send(failedResponse(request.requestId(),
                    "An error occurred while processing the request."));
        }
    }

    // Helper to build a FAILED response with no dog breed data
    private WorkerResponseDTO failedResponse(Long requestId, String reason) {
        return new WorkerResponseDTO(requestId, "FAILED", reason,
                null, null, null, null, null);
    }
}
