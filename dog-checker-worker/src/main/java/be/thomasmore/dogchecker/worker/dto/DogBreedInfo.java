package be.thomasmore.dogchecker.worker.dto;

// Groups all dog breed display fields into a single object
public record DogBreedInfo(
        String imageLink,
        int goodWithChildren,
        int goodWithOtherDogs,
        int goodWithStrangers,
        int shedding,
        int grooming,
        int drooling,
        int coatLength,
        int playfulness,
        int protectiveness,
        int trainability,
        int energy,
        int barking
) {}
