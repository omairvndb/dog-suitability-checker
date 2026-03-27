package be.thomasmore.dogchecker.api.service;

import be.thomasmore.dogchecker.api.dto.CheckRequestDTO;
import be.thomasmore.dogchecker.api.entity.DogWeatherRequest;
import be.thomasmore.dogchecker.api.entity.DogWeatherRequest.Status;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DogCheckerService {

    @Transactional
    public DogWeatherRequest createCheck(CheckRequestDTO dto) {
        DogWeatherRequest request = new DogWeatherRequest();
        request.breed = dto.breed();
        request.city = dto.city();
        request.status = Status.PENDING;
        request.persist();
        return request;
    }

    public DogWeatherRequest getStatus(Long id) {
        return DogWeatherRequest.findById(id);
    }

    public DogWeatherRequest getResult(Long id) {
        return DogWeatherRequest.findById(id);
    }
}
