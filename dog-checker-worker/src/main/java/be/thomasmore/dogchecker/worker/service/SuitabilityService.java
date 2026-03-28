package be.thomasmore.dogchecker.worker.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SuitabilityService {

    public record SuitabilityResult(String suitability, String reason) {}

    public SuitabilityResult evaluate(int coatLength, int energy, double tempC) {
        // Rule 1: Hot weather + long coat
        if (tempC > 25 && coatLength >= 4) {
            return new SuitabilityResult("BAD", "Long-coated dogs struggle in hot weather above 25°C.");
        }
        // Rule 2: Cold weather + short coat
        if (tempC < 5 && coatLength <= 2) {
            return new SuitabilityResult("BAD", "Short-coated dogs are not suited for temperatures below 5°C.");
        }
        // Rule 3: High energy dog in moderate climate
        if (energy >= 4 && tempC >= 5 && tempC <= 20) {
            return new SuitabilityResult("GOOD", "High-energy dogs thrive in moderate, cool climates perfect for outdoor activity.");
        }
        // Rule 4: High energy dog in hot climate
        if (energy >= 4 && tempC > 25) {
            return new SuitabilityResult("MODERATE", "High-energy dogs can live in hot climates but need extra care and hydration.");
        }
        // Rule 5: Default
        return new SuitabilityResult("MODERATE", "No extreme conditions detected. The dog should be comfortable with proper care.");
    }
}
