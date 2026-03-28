package be.thomasmore.dogchecker.worker.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class DogApiHealthCheck implements HealthCheck {

    @ConfigProperty(name = "dogs.api.key")
    String dogsApiKey;

    @Override
    public HealthCheckResponse call() {
        if (dogsApiKey != null && !dogsApiKey.isBlank()) {
            return HealthCheckResponse.up("Dogs API key is configured");
        }
        return HealthCheckResponse.named("Dogs API").down()
                .withData("error", "API key is not configured").build();
    }
}
