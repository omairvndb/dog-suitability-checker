package be.thomasmore.dogchecker.api.resource;

import be.thomasmore.dogchecker.api.dto.CheckRequestDTO;
import be.thomasmore.dogchecker.api.entity.DogWeatherRequest;
import be.thomasmore.dogchecker.api.entity.DogWeatherRequest.Status;
import be.thomasmore.dogchecker.api.service.DogCheckerService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DogCheckerResource {

    @Inject
    DogCheckerService service;

    @POST
    @Path("/check")
    public DogWeatherRequest check(CheckRequestDTO dto) {
        return service.createCheck(dto);
    }

    @GET
    @Path("/status/{id}")
    public DogWeatherRequest status(@PathParam("id") Long id) {
        DogWeatherRequest request = service.getStatus(id);
        if (request == null) {
            throw new NotFoundException("Request not found");
        }
        return request;
    }

    @GET
    @Path("/result/{id}")
    public DogWeatherRequest result(@PathParam("id") Long id) {
        DogWeatherRequest request = service.getResult(id);
        if (request == null) {
            throw new NotFoundException("Request not found");
        }
        if (request.status != Status.DONE && request.status != Status.FAILED) {
            throw new WebApplicationException("Result not ready yet. Current status: " + request.status, 409);
        }
        return request;
    }
}
