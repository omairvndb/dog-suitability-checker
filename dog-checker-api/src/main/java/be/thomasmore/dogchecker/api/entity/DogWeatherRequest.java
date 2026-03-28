package be.thomasmore.dogchecker.api.entity;

import be.thomasmore.dogchecker.api.dto.DogBreedInfo;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
public class DogWeatherRequest extends PanacheEntity {

    public String breed;

    public String city;

    @Enumerated(EnumType.STRING)
    public Status status;

    @Enumerated(EnumType.STRING)
    public Suitability suitability;

    public String reason;

    public String matchedBreed;

    public String matchedCity;

    public Double temperature;

    public Integer humidity;

    @Embedded
    public DogBreedInfo breedInfo;

    public enum Status {
        PENDING, PROCESSING, DONE, FAILED
    }

    public enum Suitability {
        GOOD, BAD, MODERATE
    }
}
