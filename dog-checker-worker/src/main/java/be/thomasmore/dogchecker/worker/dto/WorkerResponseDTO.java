package be.thomasmore.dogchecker.worker.dto;

public record WorkerResponseDTO(Long requestId, String suitability, String reason) {
}
