package be.thomasmore.dogchecker.api.health;

import be.thomasmore.dogchecker.api.entity.DogWeatherRequest;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        try {
            DogWeatherRequest.count();
            return HealthCheckResponse.up("Database connection is healthy");
        } catch (Exception e) {
            return HealthCheckResponse.down("Database connection is unhealthy");
        }
    }
}
