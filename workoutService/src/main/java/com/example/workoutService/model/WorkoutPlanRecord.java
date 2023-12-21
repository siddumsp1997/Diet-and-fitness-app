package com.example.workoutService.model;

public class WorkoutPlanRecord {

    private int userID;

    private int workoutPlanID;

    private int workoutID;

    private int targetUnitsPerDay;

    private double targetCalorieCount;

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
