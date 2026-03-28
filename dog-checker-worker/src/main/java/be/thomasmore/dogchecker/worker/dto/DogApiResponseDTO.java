package be.thomasmore.dogchecker.worker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DogApiResponseDTO {

    public String name;

    @JsonProperty("image_link")
    public String imageLink;

    // Traits (1-5 scale)
    @JsonProperty("good_with_children")
    public int goodWithChildren;

    @JsonProperty("good_with_other_dogs")
    public int goodWithOtherDogs;

    @JsonProperty("good_with_strangers")
    public int goodWithStrangers;

    public int shedding;
    public int grooming;
    public int drooling;

    @JsonProperty("coat_length")
    public int coatLength;

    public int playfulness;
    public int protectiveness;
    public int trainability;
    public int energy;
    public int barking;

    // Used by SuitabilityService for weight-based scoring
    @JsonProperty("max_weight_male")
    public double maxWeightMale;
}
