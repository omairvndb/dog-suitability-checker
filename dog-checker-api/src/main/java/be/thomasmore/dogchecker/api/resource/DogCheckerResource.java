package be.thomasmore.dogchecker.api.resource;

import be.thomasmore.dogchecker.api.dto.CheckRequestDTO;
import be.thomasmore.dogchecker.api.dto.CheckResponseDTO;
import be.thomasmore.dogchecker.api.dto.ResultResponseDTO;
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
    public CheckResponseDTO check(CheckRequestDTO dto) {
        DogWeatherRequest request = service.createCheck(dto);
        return new CheckResponseDTO(request.id, request.status.name());
    }

    @GET
    @Path("/status/{id}")
    public CheckResponseDTO status(@PathParam("id") Long id) {
        DogWeatherRequest request = service.getStatus(id);
        if (request == null) {
            throw new NotFoundException("Request not found");
        }
        return new CheckResponseDTO(request.id, request.status.name());
    }

    @GET
    @Path("/result/{id}")
    public ResultResponseDTO result(@PathParam("id") Long id) {
        DogWeatherRequest request = service.getResult(id);
        if (request == null) {
            throw new NotFoundException("Request not found");
        }
        if (request.status != Status.DONE && request.status != Status.FAILED) {
            throw new WebApplicationException("Result not ready yet. Current status: " + request.status, 409);
        }
        return new ResultResponseDTO(
                request.id,
                request.breed,
                request.city,
                request.status.name(),
                request.suitability != null ? request.suitability.name() : null,
                request.reason
        );
    }
}
