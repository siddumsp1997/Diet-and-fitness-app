package com.example.workoutService.model;

import java.util.ArrayList;


public class WorkoutProgressDocument {

    private int userID;

    private ArrayList<UserWorkoutRecord> userWorkoutRecords;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    

    public ArrayList<UserWorkoutRecord> getMealIntakeRecords() {
        return userWorkoutRecords;
    }

    public void setMealIntakeRecords(ArrayList<UserWorkoutRecord> userWorkoutRecords) {
        this.userWorkoutRecords = userWorkoutRecords;
    }

}