package be.thomasmore.dogchecker.worker.service;

import be.thomasmore.dogchecker.worker.dto.DogApiResponseDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

@RegisterRestClient(configKey = "dogs-api")
@Path("/v1")
public interface DogApiClient {

    @GET
    @Path("/dogs")
    List<DogApiResponseDTO> getBreed(@QueryParam("name") String breed,
                                     @HeaderParam("X-Api-Key") String apiKey);
}
