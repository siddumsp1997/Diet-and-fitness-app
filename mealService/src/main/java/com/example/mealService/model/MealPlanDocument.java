package com.example.mealService.model;

import java.util.ArrayList;

public class MealPlanDocument {

    private int userID;

    private ArrayList<MealPlanRecord> mealPlanRecords;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    

    public ArrayList<MealPlanRecord> getMealPlanRecords() {
        return mealPlanRecords;
    }

    public void setMealPlanRecords(ArrayList<MealPlanRecord> mealPlanRecords) {
        this.mealPlanRecords = mealPlanRecords;
    }

}