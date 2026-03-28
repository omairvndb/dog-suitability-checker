package be.thomasmore.dogchecker.worker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DogApiResponseDTO {

    public String name;
    public int energy;

    @JsonProperty("coat_length")
    public int coatLength;

    @JsonProperty("max_weight_male")
    public double maxWeight;
}
