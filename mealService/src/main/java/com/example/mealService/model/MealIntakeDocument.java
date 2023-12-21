package com.example.mealService.model;

import java.util.ArrayList;

public class MealIntakeDocument {

    private int userID;

    private ArrayList<UserMealIntakeRecord> mealIntakeRecords;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    

    public ArrayList<UserMealIntakeRecord> getMealIntakeRecords() {
        return mealIntakeRecords;
    }

    public void setMealIntakeRecords(ArrayList<UserMealIntakeRecord> mealIntakeRecords) {
        this.mealIntakeRecords = mealIntakeRecords;
    }

}