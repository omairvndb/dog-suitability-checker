package be.thomasmore.dogchecker.api.dto;

import jakarta.persistence.Embeddable;

// Groups all dog breed display fields into a single object.
// @Embeddable allows Hibernate to flatten these into columns on the parent entity.
@Embeddable
public class DogBreedInfo {

    public String imageLink;
    public Integer goodWithChildren;
    public Integer goodWithOtherDogs;
    public Integer goodWithStrangers;
    public Integer shedding;
    public Integer grooming;
    public Integer drooling;
    public Integer coatLength;
    public Integer playfulness;
    public Integer protectiveness;
    public Integer trainability;
    public Integer energy;
    public Integer barking;

    public DogBreedInfo() {}

    public DogBreedInfo(String imageLink, int goodWithChildren, int goodWithOtherDogs,
                        int goodWithStrangers, int shedding, int grooming, int drooling,
                        int coatLength, int playfulness, int protectiveness, int trainability,
                        int energy, int barking) {
        this.imageLink = imageLink;
        this.goodWithChildren = goodWithChildren;
        this.goodWithOtherDogs = goodWithOtherDogs;
        this.goodWithStrangers = goodWithStrangers;
        this.shedding = shedding;
        this.grooming = grooming;
        this.drooling = drooling;
        this.coatLength = coatLength;
        this.playfulness = playfulness;
        this.protectiveness = protectiveness;
        this.trainability = trainability;
        this.energy = energy;
        this.barking = barking;
    }
}
