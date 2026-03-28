package be.thomasmore.dogchecker.worker.health;

import be.thomasmore.dogchecker.worker.service.DogApiClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Readiness
@ApplicationScoped
public class DogApiHealthCheck implements HealthCheck {

    @Inject
    @RestClient
    DogApiClient dogApiClient;

    @ConfigProperty(name = "dogs.api.key")
    String dogsApiKey;

    @Override
    public HealthCheckResponse call() {
        try {
            dogApiClient.getBreed("labrador", dogsApiKey);
            return HealthCheckResponse.up("Dogs API is healthy");
        } catch (Exception e) {
            return HealthCheckResponse.named("Dogs API is unhealthy").down()
                    .withData("error", e.getMessage()).build();
        }
    }
}
