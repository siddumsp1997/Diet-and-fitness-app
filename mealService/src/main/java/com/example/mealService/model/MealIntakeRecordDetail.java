package com.example.mealService.model;

public class MealIntakeRecordDetail {

    public String mealName;
    
    public int userID;

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getMealID() {
        return mealID;
    }

    public void setMealID(int mealID) {
        this.mealID = mealID;
    }

    public int getMealPlanID() {
        return mealPlanID;
    }

    public void setMealPlanID(int mealPlanID) {
        this.mealPlanID = mealPlanID;
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

    public int mealID;

    public int mealPlanID;

    public int unitsDone;

    public String dateOfMeal;
    
    public String mealType;
    

}

