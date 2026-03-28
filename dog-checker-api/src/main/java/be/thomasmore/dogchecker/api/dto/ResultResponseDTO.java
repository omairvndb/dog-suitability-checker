package be.thomasmore.dogchecker.api.dto;

public record ResultResponseDTO(
        Long id,
        String breed,
        String city,
        String status,
        String suitability,
        String reason,
        String matchedBreed,
        String matchedCity,
        Double temperature,
        Integer humidity,
        DogBreedInfo breedInfo
) {}
