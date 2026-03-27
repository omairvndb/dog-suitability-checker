package be.thomasmore.dogchecker.api.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DogWeatherRequest extends PanacheEntity {

    public String breed;

    public String city;

    @Enumerated(EnumType.STRING)
    public Status status;

    @Enumerated(EnumType.STRING)
    public Suitability suitability;

    public String reason;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Status {
        PENDING, PROCESSING, DONE, FAILED
    }

    public enum Suitability {
        GOOD, BAD, MODERATE
    }
}
