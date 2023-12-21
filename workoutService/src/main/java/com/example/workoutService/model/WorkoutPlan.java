package com.example.workoutService.model;

public class WorkoutPlan {

    private int userID;

    private int workoutPlanID;

    private String workoutPlanName;

    private String dateOfCreation;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getWorkoutPlanID() {
        return workoutPlanID;
    }

    public void setWorkoutPlanID(int workoutPlanID) {
        this.workoutPlanID = workoutPlanID;
    }

    public String getWorkoutPlanName() {
        return workoutPlanName;
    }

    public void setWorkoutPlanName(String workoutPlanName) {
        this.workoutPlanName = workoutPlanName;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

}
