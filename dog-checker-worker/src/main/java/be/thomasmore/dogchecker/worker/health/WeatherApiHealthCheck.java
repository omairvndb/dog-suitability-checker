package be.thomasmore.dogchecker.worker.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

@Readiness
@ApplicationScoped
public class WeatherApiHealthCheck implements HealthCheck {

    @ConfigProperty(name = "weather.api.key")
    String weatherApiKey;

    @Override
    public HealthCheckResponse call() {
        if (weatherApiKey != null && !weatherApiKey.isBlank()) {
            return HealthCheckResponse.up("Weather API key is configured");
        }
        return HealthCheckResponse.named("Weather API").down()
                .withData("error", "API key is not configured").build();
    }
}
