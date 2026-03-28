package be.thomasmore.dogchecker.worker.service;

import be.thomasmore.dogchecker.worker.dto.WeatherApiResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class WeatherApiService {

    private static final Logger LOG = Logger.getLogger(WeatherApiService.class);

    @Inject
    @RestClient
    WeatherApiClient weatherApiClient;

    @ConfigProperty(name = "weather.api.key")
    String apiKey;

    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.75, delay = 10000)
    @Retry(maxRetries = 3, delay = 1000)
    @Fallback(fallbackMethod = "fetchWeatherFallback")
    public WeatherApiResponseDTO fetchWeather(String city) {
        return weatherApiClient.getWeather(apiKey, city);
    }

    WeatherApiResponseDTO fetchWeatherFallback(String city) {
        LOG.errorf("Weather API call failed after retries for city: %s", city);
        return null;
    }
}
