package be.thomasmore.dogchecker.worker.service;

import be.thomasmore.dogchecker.worker.dto.DogApiResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import java.util.List;

@ApplicationScoped
public class DogApiService {

    private static final Logger LOG = Logger.getLogger(DogApiService.class);

    @Inject
    @RestClient
    DogApiClient dogApiClient;

    @ConfigProperty(name = "dogs.api.key")
    String apiKey;

    @Retry(maxRetries = 3, delay = 1000)
    @Fallback(fallbackMethod = "fetchBreedFallback")
    public List<DogApiResponseDTO> fetchBreed(String breed) {
        return dogApiClient.getBreed(breed, apiKey);
    }

    List<DogApiResponseDTO> fetchBreedFallback(String breed) {
        LOG.errorf("Dogs API call failed after retries for breed: %s", breed);
        return null;
    }
}
