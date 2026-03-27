package be.thomasmore.dogchecker.api.dto;

public record ResultResponseDTO(Long id, String breed, String city, String status, String suitability, String reason) {
}
