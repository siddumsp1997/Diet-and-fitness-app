package com.example.workoutService.model;

public class UserWorkoutRecord {

    private int userID;

    private int workoutPlanID;

    private int workoutID;

    private int unitsDone;

    private String dateOfWorkout;

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

    public int getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    public int getUnitsDone() {
        return unitsDone;
    }

    public void setUnitsDone(int unitsDone) {
        this.unitsDone = unitsDone;
    }

    public String getDateOfWorkout() {
        return dateOfWorkout;
    }

    public void setDateOfWorkout(String dateOfWorkout) {
        this.dateOfWorkout = dateOfWorkout;
    }
}
