package be.thomasmore.dogchecker.worker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherApiResponseDTO {

    public Location location;
    public Current current;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        public String name;
        public String country;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Current {

        @JsonProperty("temp_c")
        public double tempC;

        public int humidity;

        public Condition condition;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Condition {
        public String text;
    }
}
