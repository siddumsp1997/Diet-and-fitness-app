package com.example.workoutService.model;

public class Workout {

    private int workoutID;

    private String name;

    private double caloriesBurntPerUnit;

    private String description;

    public int getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCaloriesBurntPerUnit() {
        return caloriesBurntPerUnit;
    }

    public void setCaloriesBurntPerUnit(double caloriesBurntPerUnit) {
        this.caloriesBurntPerUnit = caloriesBurntPerUnit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}