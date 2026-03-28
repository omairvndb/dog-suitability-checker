package be.thomasmore.dogchecker.api.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import be.thomasmore.dogchecker.api.dto.CheckRequestDTO;
import be.thomasmore.dogchecker.api.dto.WorkerRequestDTO;
import be.thomasmore.dogchecker.api.dto.WorkerResponseDTO;
import be.thomasmore.dogchecker.api.entity.DogWeatherRequest;
import be.thomasmore.dogchecker.api.entity.DogWeatherRequest.Status;
import be.thomasmore.dogchecker.api.entity.DogWeatherRequest.Suitability;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DogCheckerService {

    private static final Logger LOG = Logger.getLogger(DogCheckerService.class);

    @Inject
    MeterRegistry registry;

    private Counter checksReceivedCounter;

    @Channel("check-request")
    Emitter<WorkerRequestDTO> emitter;

    // Initialize the checks received counter metric
    @PostConstruct
    void init() {
        checksReceivedCounter = Counter.builder("dog.checks.received").register(registry);
    }

    @Transactional
    public DogWeatherRequest createCheck(CheckRequestDTO dto) {
        DogWeatherRequest request = new DogWeatherRequest();
        request.breed = dto.breed();
        request.city = dto.city();
        request.status = Status.PENDING;
        request.persist(); 

        emitter.send(new WorkerRequestDTO(request.id, request.breed, request.city));

        checksReceivedCounter.increment();

        return request;
    }

    public DogWeatherRequest getStatus(Long id) {
        return DogWeatherRequest.findById(id);
    }

    public DogWeatherRequest getResult(Long id) {
        return DogWeatherRequest.findById(id);
    }

    @Incoming("check-response")
    @Transactional
    public void processResponse(WorkerResponseDTO response) {
        LOG.infof("Received response for request %d: %s", response.requestId(), response.suitability());

        DogWeatherRequest request = DogWeatherRequest.findById(response.requestId());
        if (request == null) {
            LOG.errorf("Request %d not found", response.requestId());
            return;
        }

        if ("FAILED".equals(response.suitability())) {
            request.status = Status.FAILED;
        } else {
            request.status = Status.DONE;
            request.suitability = Suitability.valueOf(response.suitability());
        }
        request.reason = response.reason();
        request.matchedBreed = response.matchedBreed();
        request.matchedCity = response.matchedCity();
        request.temperature = response.temperature();
        request.humidity = response.humidity();
        request.breedInfo = response.breedInfo();
    }
}
