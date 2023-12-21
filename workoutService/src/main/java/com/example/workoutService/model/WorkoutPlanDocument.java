package com.example.workoutService.model;

import java.util.ArrayList;

public class WorkoutPlanDocument {

    private int userID;

    private ArrayList<WorkoutPlanRecord> workoutPlanRecords;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    

    public ArrayList<WorkoutPlanRecord> getWorkoutPlanRecords() {
        return workoutPlanRecords;
    }

    public void setWorkoutPlanRecords(ArrayList<WorkoutPlanRecord> workoutPlanRecords) {
        this.workoutPlanRecords = workoutPlanRecords;
    }

}