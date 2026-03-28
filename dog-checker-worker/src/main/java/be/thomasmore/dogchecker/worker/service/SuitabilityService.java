package be.thomasmore.dogchecker.worker.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SuitabilityService {

    public record SuitabilityResult(String suitability, String reason) {}

    public SuitabilityResult evaluate(int coatLength, int energy, double maxWeight, double tempC, int humidity) {
        int score = 0;
        List<String> reasons = new ArrayList<>();

        // Coat length vs temperature
        if (tempC > 30 && coatLength >= 3) {
            score -= 3;
            reasons.add("Thick coat is a serious problem in extreme heat.");
        } else if (tempC > 25 && coatLength >= 3) {
            score -= 2;
            reasons.add("Thick-coated dogs struggle in hot weather.");
        } else if (tempC < 0 && coatLength <= 2) {
            score -= 3;
            reasons.add("Short-coated dogs suffer in freezing temperatures.");
        } else if (tempC < 5 && coatLength <= 2) {
            score -= 2;
            reasons.add("Short coat offers little protection in cold weather.");
        } else if (tempC >= 5 && tempC <= 20 && coatLength >= 3) {
            score += 2;
            reasons.add("Thick coat is well suited for cool weather.");
        } else if (tempC >= 10 && tempC <= 25 && coatLength <= 2) {
            score += 1;
            reasons.add("Short coat is comfortable in mild weather.");
        }

        // Energy vs temperature
        if (energy >= 4 && tempC >= 5 && tempC <= 20) {
            score += 2;
            reasons.add("Moderate climate is great for an active dog.");
        } else if (energy >= 4 && tempC > 30) {
            score -= 2;
            reasons.add("High-energy dogs overheat quickly in extreme heat.");
        } else if (energy >= 4 && tempC > 25) {
            score -= 1;
            reasons.add("Active dogs need extra hydration in warm weather.");
        } else if (energy <= 2 && tempC >= 10 && tempC <= 25) {
            score += 1;
            reasons.add("Low-energy dogs do well in comfortable temperatures.");
        }

        // Humidity impact
        if (humidity > 80 && tempC > 25) {
            score -= 2;
            reasons.add("High humidity combined with heat makes cooling difficult.");
        } else if (humidity > 70 && tempC > 25) {
            score -= 1;
            reasons.add("Humid and warm conditions can be uncomfortable.");
        }

        // Weight in extreme heat
        if (maxWeight > 40 && tempC > 25) {
            score -= 1;
            reasons.add("Large dogs are more prone to overheating.");
        } else if (maxWeight < 10 && tempC < 5) {
            score -= 1;
            reasons.add("Small dogs lose body heat quickly in the cold.");
        }

        // Determine verdict
        String suitability;
        if (score >= 2) {
            suitability = "GOOD";
        } else if (score <= -2) {
            suitability = "BAD";
        } else {
            suitability = "MODERATE";
        }

        // Build reason string
        String reason;
        if (reasons.isEmpty()) {
            reason = "No extreme conditions detected. The dog should be comfortable with proper care.";
        } else {
            reason = String.join(" ", reasons);
        }

        return new SuitabilityResult(suitability, reason);
    }
}
