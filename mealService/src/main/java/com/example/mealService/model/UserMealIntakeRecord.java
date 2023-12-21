package com.example.mealService.model;

public class UserMealIntakeRecord {

    private int userID;

    private int mealPlanID;

    private int mealID;

    private int unitsDone;

    private String dateOfMeal;
    
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

    public int getUnitsDone() {
        return unitsDone;
    }

    public void setUnitsDone(int unitsDone) {
        this.unitsDone = unitsDone;
    }

    public String getDateOfMeal() {
        return dateOfMeal;
    }

    public void setDateOfMeal(String dateOfMeal) {
        this.dateOfMeal = dateOfMeal;
    }
    
    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
}
