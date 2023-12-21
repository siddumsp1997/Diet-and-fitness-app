package com.example.mealService.model;

public class Meal {

    private int mealID;

    private String name;

    private double caloriesBurntPerUnit;

    private String description;

    public int getMealID() {
        return mealID;
    }

    public void setMealID(int mealID) {
        this.mealID = mealID;
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