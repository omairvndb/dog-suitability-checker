package be.thomasmore.dogchecker.api.health;

import be.thomasmore.dogchecker.api.entity.DogWeatherRequest;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class ApiHealthCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        try {
            // Check if we can access the database by using a simple query like counting 
            // the number of DogWeatherRequest entries
            DogWeatherRequest.count();
            return HealthCheckResponse.up("Database connection is healthy");
        } catch (Exception e) {
            return HealthCheckResponse.down("Database connection is unhealthy");
        }
    }
}
