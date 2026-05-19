package be.thomasmore.dogchecker.worker.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SuitabilityService {

    private static final double EXTREME_HEAT_C  = 30.0;
    private static final double WARM_C          = 25.0;
    private static final double COOL_MAX_C      = 20.0;
    private static final double MILD_MIN_C      = 10.0;
    private static final double COLD_C          = 5.0;
    private static final double FREEZING_C      = 0.0;

    private static final int HIGH_HUMIDITY_PCT  = 80;
    private static final int WARM_HUMIDITY_PCT  = 70;

    private static final double LARGE_DOG_LBS   = 40.0;
    private static final double SMALL_DOG_LBS   = 10.0;

    private static final int THICK_COAT         = 3;
    private static final int SHORT_COAT         = 2;
    private static final int HIGH_ENERGY        = 4;
    private static final int LOW_ENERGY         = 2;

    private static final int GOOD_THRESHOLD     = 2;
    private static final int BAD_THRESHOLD      = -2;

    public record SuitabilityResult(String suitability, String reason) {}

    public SuitabilityResult evaluate(int coatLength, int energy, double maxWeight, double tempC, int humidity) {
        int score = 0;
        List<String> reasons = new ArrayList<>();

        // Coat length vs temperature
        if (tempC > EXTREME_HEAT_C && coatLength >= THICK_COAT) {
            score -= 3;
            reasons.add("Thick coat is a serious problem in extreme heat.");
        } else if (tempC > WARM_C && coatLength >= THICK_COAT) {
            score -= 2;
            reasons.add("Thick-coated dogs struggle in hot weather.");
        } else if (tempC < FREEZING_C && coatLength <= SHORT_COAT) {
            score -= 3;
            reasons.add("Short-coated dogs suffer in freezing temperatures.");
        } else if (tempC < COLD_C && coatLength <= SHORT_COAT) {
            score -= 2;
            reasons.add("Short coat offers little protection in cold weather.");
        } else if (tempC >= COLD_C && tempC <= COOL_MAX_C && coatLength >= THICK_COAT) {
            score += 2;
            reasons.add("Thick coat is well suited for cool weather.");
        } else if (tempC >= MILD_MIN_C && tempC <= WARM_C && coatLength <= SHORT_COAT) {
            score += 1;
            reasons.add("Short coat is comfortable in mild weather.");
        }

        // Energy vs temperature
        if (energy >= HIGH_ENERGY && tempC >= COLD_C && tempC <= COOL_MAX_C) {
            score += 2;
            reasons.add("Moderate climate is great for an active dog.");
        } else if (energy >= HIGH_ENERGY && tempC > EXTREME_HEAT_C) {
            score -= 2;
            reasons.add("High-energy dogs overheat quickly in extreme heat.");
        } else if (energy >= HIGH_ENERGY && tempC > WARM_C) {
            score -= 1;
            reasons.add("Active dogs need extra hydration in warm weather.");
        } else if (energy <= LOW_ENERGY && tempC >= MILD_MIN_C && tempC <= WARM_C) {
            score += 1;
            reasons.add("Low-energy dogs do well in comfortable temperatures.");
        }

        // Humidity impact
        if (humidity > HIGH_HUMIDITY_PCT && tempC > WARM_C) {
            score -= 2;
            reasons.add("High humidity combined with heat makes cooling difficult.");
        } else if (humidity > WARM_HUMIDITY_PCT && tempC > WARM_C) {
            score -= 1;
            reasons.add("Humid and warm conditions can be uncomfortable.");
        }

        // Weight in extreme heat or cold
        if (maxWeight > LARGE_DOG_LBS && tempC > WARM_C) {
            score -= 1;
            reasons.add("Large dogs are more prone to overheating.");
        } else if (maxWeight < SMALL_DOG_LBS && tempC < COLD_C) {
            score -= 1;
            reasons.add("Small dogs lose body heat quickly in the cold.");
        }

        // Determine verdict
        String suitability;
        if (score >= GOOD_THRESHOLD) {
            suitability = "GOOD";
        } else if (score <= BAD_THRESHOLD) {
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
