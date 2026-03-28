package be.thomasmore.dogchecker.api.dto;

public record WorkerResponseDTO(
        Long requestId,
        String suitability,
        String reason,
        String matchedBreed,
        String matchedCity,
        Double temperature,
        Integer humidity
) {}
