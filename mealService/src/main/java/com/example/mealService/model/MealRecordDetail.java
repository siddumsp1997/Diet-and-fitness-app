package com.example.mealService.model;

public class MealRecordDetail {

    private String mealName;

    private String mealPlanName;

    private int mealId;

    private int mealPlanId;

    private int targetUnitsPerDay;

    private String description;
    
    private String mealType;

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public int getMealPlanId() {
        return mealPlanId;
    }

    public void setMealPlanId(int mealPlanId) {
        this.mealPlanId = mealPlanId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTargetUnitsPerDay() {
        return targetUnitsPerDay;
    }

    public void setTargetUnitsPerDay(int targetUnitsPerDay) {
        this.targetUnitsPerDay = targetUnitsPerDay;
    }

    public String getMealPlanName() {
        return mealPlanName;
    }

    public void setMealPlanName(String mealPlanName) {
        this.mealPlanName = mealPlanName;
    }

    public String getName() {
        return mealName;
    }
    public void setName(String mealName) {
        this.mealName = mealName;
    }
    
    public String getMealType() {
        return mealType;
    }
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

}