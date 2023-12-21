package com.example.workoutService.model;

public class WorkoutProgressRecordDetail {

    private String workoutName;

    private int workoutId;

    private int workoutPlanId;

    private int noOfUnitsPerformed;

    private String dateOfWorkout;

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getWorkoutPlanId() {
        return workoutPlanId;
    }

    public void setWorkoutPlanId(int workoutPlanId) {
        this.workoutPlanId = workoutPlanId;
    }

    public int getNoOfUnitsPerformed() {
        return noOfUnitsPerformed;
    }

    public void setNoOfUnitsPerformed(int noOfUnitsPerformed) {
        this.noOfUnitsPerformed = noOfUnitsPerformed;
    }

    public String getDateOfWorkout() {
        return dateOfWorkout;
    }

    public void setDateOfWorkout(String dateOfWorkout) {
        this.dateOfWorkout = dateOfWorkout;
    }

    public String getName() {
        return workoutName;
    }
    public void setName(String workoutName) {
        this.workoutName = workoutName;
    }

}
