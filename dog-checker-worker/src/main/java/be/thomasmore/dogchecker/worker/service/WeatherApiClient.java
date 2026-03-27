package be.thomasmore.dogchecker.worker.service;

import be.thomasmore.dogchecker.worker.dto.WeatherApiResponseDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "weather-api")
@Path("/v1")
public interface WeatherApiClient {

    @GET
    @Path("/current.json")
    WeatherApiResponseDTO getWeather(@QueryParam("key") String apiKey,
                                     @QueryParam("q") String city);
}
