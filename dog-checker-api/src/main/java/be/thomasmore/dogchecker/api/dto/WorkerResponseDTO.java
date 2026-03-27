package be.thomasmore.dogchecker.api.dto;

public record WorkerResponseDTO(Long requestId, String suitability, String reason) {
}
