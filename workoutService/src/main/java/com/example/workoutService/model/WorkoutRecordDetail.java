package com.example.workoutService.model;

public class WorkoutRecordDetail {

    private String workoutName;

    private String workoutPlanName;

    private int workoutId;

    private int workoutPlanId;

    private int targetUnitsPerDay;

    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTargetUnitsPerDay() {
        return targetUnitsPerDay;
    }

    public void setTargetUnitsPerDay(int targetUnitsPerDay) {
        this.targetUnitsPerDay = targetUnitsPerDay;
    }

    public String getWorkoutPlanName() {
        return workoutPlanName;
    }

    public void setWorkoutPlanName(String workoutPlanName) {
        this.workoutPlanName = workoutPlanName;
    }

    public String getName() {
        return workoutName;
    }
    public void setName(String workoutName) {
        this.workoutName = workoutName;
    }

}