package com.example.mealService.model;

public class MealPlanRecord {

    private int userID;

    private int mealPlanID;

    private int mealID;

    private int targetUnitsPerDay;

    private double targetCalorieCount;
    
    private String mealType;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getMealPlanID() {
        return mealPlanID;
    }

    public void setMealPlanID(int mealPlanID) {
        this.mealPlanID = mealPlanID;
    }

    public int getMealID() {
        return mealID;
    }

    public void setMealID(int mealID) {
        this.mealID = mealID;
    }
    
    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
    

    public int getTargetUnitsPerDay() {
        return targetUnitsPerDay;
    }
    

    public void setTargetUnitsPerDay(int targetUnitsPerDay) {
        this.targetUnitsPerDay = targetUnitsPerDay;
    }

    public double getTargetCalorieCount() {
        return targetCalorieCount;
    }

    public void setTargetCalorieCount(double targetCalorieCount) {
        this.targetCalorieCount = targetCalorieCount;

    }
    
}
