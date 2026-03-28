package be.thomasmore.dogchecker.worker.health;

import be.thomasmore.dogchecker.worker.service.WeatherApiClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Readiness
@ApplicationScoped
public class WeatherApiHealthCheck implements HealthCheck {

    @Inject
    @RestClient
    WeatherApiClient weatherApiClient;

    @ConfigProperty(name = "weather.api.key")
    String weatherApiKey;

    @Override
    public HealthCheckResponse call() {
        try {
            weatherApiClient.getWeather(weatherApiKey, "London");
            return HealthCheckResponse.up("Weather API is healthy");
        } catch (Exception e) {
            return HealthCheckResponse.named("Weather API is unhealthy").down()
                    .withData("error", e.getMessage()).build();
        }
    }
}
