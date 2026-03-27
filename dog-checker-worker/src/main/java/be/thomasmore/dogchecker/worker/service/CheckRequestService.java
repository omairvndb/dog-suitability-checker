package be.thomasmore.dogchecker.worker.service;

import be.thomasmore.dogchecker.worker.dto.WorkerRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CheckRequestService {

    private static final Logger LOG = Logger.getLogger(CheckRequestService.class);

    @Incoming("check-request")
    public void processCheckRequest(WorkerRequestDTO request) {
        LOG.infof("Received check request: id=%d, breed=%s, city=%s",
                request.requestId(), request.breed(), request.city());
        // TODO: call DogApiService, WeatherApiService, SuitabilityService, then send response
    }
}
