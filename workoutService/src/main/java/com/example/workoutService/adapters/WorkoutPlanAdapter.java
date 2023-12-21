package com.example.workoutService.adapters;

import com.example.workoutService.model.UserWorkoutRecord;
import com.example.workoutService.model.Workout;
import com.example.workoutService.model.WorkoutPlan;
import com.example.workoutService.model.WorkoutPlanDocument;
import com.example.workoutService.model.WorkoutPlanRecord;
import com.example.workoutService.model.WorkoutProgressDocument;
import com.google.gson.Gson;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.sql.*;

public class WorkoutPlanAdapter {

	public Jedis redisConnectionObject;
	
	public MongoClient mongoDbConnectionObject;
	
	public WorkoutPlanAdapter() {
		
		this.redisConnectionObject = null;
        this.mongoDbConnectionObject = null;

        int port = 14128;
        String host = "redis-14128.c267.us-east-1-4.ec2.cloud.redislabs.com";
        String password = "58qS8gtaj4hDaBwHVzS7BeqdEiVMaHBu";

        // Create redis connection object using Jedis
        try (Jedis jedis = new Jedis(host, port)) {
            // If your Redis server requires authentication, uncomment the following line and replace with your credentials
            jedis.auth(password);

            System.out.println("Successfully connected to WorkoutServiceDB Redis database...");

            this.redisConnectionObject = jedis;

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        ServerApi serverApiObject = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        String connectionString = "mongodb+srv://siddumsp:nzWdQDkRvi6sWq7F@sid-cluster.iavykex.mongodb.net/?retryWrites=true&w=majority";
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApiObject)
                .build();

        try {
            MongoClient mongoClient = MongoClients.create(settings);
            this.mongoDbConnectionObject = mongoClient;
            System.out.println("Successfully connected to WorkoutServiceDB MongoDB database....");
        }
        catch (Exception e) {
        	System.out.println("Not connected to WorkoutServiceDB MongoDB database....");
            e.printStackTrace();
        }
	}
	
	
	public int fetchWorkoutPlanId(){
		int result = 0;
        try{
            result = Integer.parseInt(this.redisConnectionObject.get("LatestWorkoutPlanId"));
            //this.redisConnectionObject.set("LatestMealPlanId", Integer.toString(result + 1));
        }
        catch (Exception e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return result + 1;
    }
	
	
	public int fetchWorkoutId(){
		int result = 0;
        try{
            result = Integer.parseInt(this.redisConnectionObject.get("LatestWorkoutId"));
            //this.redisConnectionObject.set("LatestMealId", Integer.toString(result + 1));
        }
        catch (Exception e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return result + 1;
    }
	
	
	public ArrayList<WorkoutPlanRecord> getWorkoutPlanRecordsForUser(int userID) throws Exception{

        ArrayList<WorkoutPlanRecord> workoutPlanRecordList = new ArrayList<WorkoutPlanRecord>();
        
        try {
    		
        	 MongoDatabase database = this.mongoDbConnectionObject.getDatabase("fitnessdb");
             MongoCollection<Document> collection = database.getCollection("workoutPlan");

             String userIdAttribute = "userID";
             //String userIdValue = Integer.toString(userID);
             //Document query = new Document(userIdAttribute, userIdValue);
             Document query = new Document(userIdAttribute, userID);
             Document targetWorkoutPlan = null;

             MongoCursor<Document> cursor = collection.find(query).iterator();

             if(cursor.hasNext()) {

	             try {
	            	 targetWorkoutPlan = cursor.next();
                     System.out.println(targetWorkoutPlan.toJson());
	             } finally {
	                 cursor.close();
	             }

	             String targetWorkoutPlanAsJson = targetWorkoutPlan.toJson();
	             JSONParser parser = new JSONParser();
		         JSONObject workoutPlanDocument = (JSONObject)parser.parse(targetWorkoutPlanAsJson);
		         JSONArray workoutPlanRecords = (JSONArray)workoutPlanDocument.get("workoutPlanRecords");
		         
		         if(workoutPlanRecords != null) {
			         for(int i = 0; i < workoutPlanRecords.size(); i++) {
			        	 
		        		Long tmp;
		                JSONObject currentObj = (JSONObject) workoutPlanRecords.get(i);
		                tmp = Long.parseLong(currentObj.get("targetUnitsPerDay").toString());
		
		                if(tmp.intValue() != 0){
		
		                	WorkoutPlanRecord planRecord = new WorkoutPlanRecord();
		                    //tmp = (Long)currentObj.get("mealPlanId");
		                    tmp = Long.parseLong(currentObj.get("workoutPlanID").toString());
		                    planRecord.setWorkoutPlanID(tmp.intValue());
		
		                    //tmp = (Long)currentObj.get("mealId");
		                    tmp = Long.parseLong(currentObj.get("workoutID").toString());
		                    planRecord.setWorkoutID(tmp.intValue());
		
		                    //tmp = (Long)currentObj.get("quantity");
		                    tmp = Long.parseLong(currentObj.get("targetUnitsPerDay").toString());
		                    planRecord.setTargetUnitsPerDay(tmp.intValue());
		
		                    //tmp = (Long)currentObj.get("userId");
		                    tmp = Long.parseLong(currentObj.get("userID").toString());
		                    planRecord.setUserID(tmp.intValue());
		
		                    //tmp = (Long)currentObj.get("mealId");
		                    tmp = Long.parseLong(currentObj.get("workoutID").toString());
		                    Workout currentMeal = getWorkoutByID(tmp.intValue());
		                    System.out.println("workout id" + tmp);	                    
		                    //tmp = (Long)currentObj.get("quantity");
		                    tmp = Long.parseLong(currentObj.get("targetUnitsPerDay").toString());
		                    planRecord.setTargetCalorieCount(tmp.intValue() * currentMeal.getCaloriesBurntPerUnit());
		
		                    workoutPlanRecordList.add(planRecord);
		                  
		                }
			         }
		         
		         }
             }
            
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		

        return workoutPlanRecordList;
    }
	

	
	public boolean updateWorkoutPlanForUser(ArrayList<WorkoutPlanRecord> updatedRecords, int userId) throws Exception{
		
		boolean result = true;
		try {
			
    		MongoDatabase database = this.mongoDbConnectionObject.getDatabase("fitnessdb");

            // Access the collection
            MongoCollection<Document> collection = database.getCollection("workoutPlan");

    		Map<String, String> hashData = redisConnectionObject.hgetAll("workoutPlan:" + Integer.toString(userId));
    		
    		if (hashData.isEmpty()) {
                
    			Map<String, String> fieldUpdates = new HashMap<>();
    			int workoutPlanId = fetchWorkoutPlanId();
    			this.redisConnectionObject.set("LatestWorkoutPlanId", Integer.toString(workoutPlanId));
    			String currentDate = java.time.LocalDate.now().toString();
    			
                fieldUpdates.put("WorkoutPlanID", Integer.toString(workoutPlanId));
                fieldUpdates.put("WorkoutPlanName", "Workout plan");
                fieldUpdates.put("UserID", Integer.toString(userId));
                fieldUpdates.put("DateOfCreation", currentDate);
                
                String updateOperation = redisConnectionObject.hmset("workoutPlan:" + Integer.toString(userId), fieldUpdates);
                
                if ("OK".equals(updateOperation)) {
                    System.out.println("Workout Plan added for user ID : " + Integer.toString(userId));
                } else {
                	System.out.println("Failed to add Workout Plan for user ID : " + Integer.toString(userId));
                }
    			
            } else {
                
                // Define the filter to identify the document to be deleted
                String userIdAttribute = "userID";
                var filter = Filters.eq(userIdAttribute, userId);

                collection.deleteOne(filter);
            }
    		
    		
    		WorkoutPlanDocument workoutPlanDocument = new WorkoutPlanDocument();
    		workoutPlanDocument.setUserID(userId);
    		workoutPlanDocument.setWorkoutPlanRecords(updatedRecords);
    		
    		Gson gson = new Gson();
    		String documentAsString = gson.toJson(workoutPlanDocument);
    		
    		Document documentToBeAdded = Document.parse(documentAsString);
            collection.insertOne(documentToBeAdded);
    		
            
    	} catch (Exception e) {
    		e.printStackTrace();
    		result = false;
    	}
		
		return result;
	}
	
	
	public boolean updateWorkout(Workout workout) {
    	
		try {
    		
    		ArrayList<Workout> workoutList = new ArrayList<Workout>();
    		int mealCount = Integer.parseInt(this.redisConnectionObject.get("LatestWorkoutId"));
    		int isAlreadyExisting = 0;
    		
    		for(int i = 1; i <= mealCount; i++) {
    			
    			String hashKey = "workout:" + Integer.toString(i);
    			
				Map<String, String> hashData = redisConnectionObject.hgetAll(hashKey);
				
				if(hashData.get("Name") != null && hashData.get("Name").equals(workout.getName())) {
					
					isAlreadyExisting = 1;
					
					Map<String, String> fieldUpdates = new HashMap<>();
	    			fieldUpdates.put("WorkoutID", Integer.toString(i));
	    			fieldUpdates.put("Name", workout.getName());
	    			fieldUpdates.put("CaloriesBurntPerUnit",  Double.toString(workout.getCaloriesBurntPerUnit()));
	    			fieldUpdates.put("Description", workout.getDescription());
	    			
	    			redisConnectionObject.hmset("workout:" + Integer.toString(i), fieldUpdates);
					break;
				}
    			
    		}
    		
    		if(isAlreadyExisting == 0) {
    			int workoutID = fetchWorkoutId();
    			this.redisConnectionObject.set("LatestWorkoutId", Integer.toString(workoutID));
    			
    			Map<String, String> fieldUpdates = new HashMap<>();
    			fieldUpdates.put("WorkoutID", Integer.toString(workoutID));
    			fieldUpdates.put("Name", workout.getName());
    			fieldUpdates.put("CaloriesBurntPerUnit",  Double.toString(workout.getCaloriesBurntPerUnit()));
    			fieldUpdates.put("Description", workout.getDescription());
    			
    			redisConnectionObject.hmset("workout:" + Integer.toString(workoutID), fieldUpdates);
    		}
    		
    		
            return true;
            
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return false;
    }
	
	
	public boolean addUserWorkoutRecord(ArrayList<UserWorkoutRecord> userWorkoutRecordList) throws Exception{
		
		boolean result = true;
		try {
			
    		MongoDatabase database = this.mongoDbConnectionObject.getDatabase("fitnessdb");

            // Access the collection
            MongoCollection<Document> collection = database.getCollection("workoutProgress");
            
            String userIdAttribute = "userID";
            String userIdValue = Integer.toString(userWorkoutRecordList.get(0).getUserID());
            //var filter = Filters.eq(userIdAttribute, userIdValue);
            var filter = Filters.eq(userIdAttribute, userWorkoutRecordList.get(0).getUserID());
            
            System.out.println("user ID value");
            System.out.println(userIdValue);
            
            ArrayList<UserWorkoutRecord> userWorkoutRecords = getUserWorkoutRecordList(userWorkoutRecordList.get(0).getUserID());
            
            //
            System.out.println(userWorkoutRecords.size());
            
            for(int i = 0; i < userWorkoutRecordList.size(); i++) {
            	userWorkoutRecords.add(userWorkoutRecordList.get(i));
            }

	        //userMealIntakeRecords.add(userMealIntakeRecord);
            System.out.println(userWorkoutRecords.size());
            
            
            collection.deleteOne(filter);
            
            
    		WorkoutProgressDocument updatedWorkoutProgressDocument = new WorkoutProgressDocument();
    		updatedWorkoutProgressDocument.setUserID(userWorkoutRecordList.get(0).getUserID());
    		updatedWorkoutProgressDocument.setMealIntakeRecords(userWorkoutRecords);
    		
    		Gson gson = new Gson();
    		String documentAsString = gson.toJson(updatedWorkoutProgressDocument);
    		
    		Document documentToBeAdded = Document.parse(documentAsString);
            collection.insertOne(documentToBeAdded);
    		
            
    	} catch (Exception e) {
    		e.printStackTrace();
    		result = false;
    	}
		
		return result;
	}
	
	
	public ArrayList<UserWorkoutRecord> getUserWorkoutRecordList(int userID) throws Exception{
		
		ArrayList<UserWorkoutRecord> userWorkoutRecordList = new ArrayList<UserWorkoutRecord>();
		
		try {
			
    		MongoDatabase database = this.mongoDbConnectionObject.getDatabase("fitnessdb");

            // Access the collection
            MongoCollection<Document> collection = database.getCollection("workoutProgress");
            
            String userIdAttribute = "userID";
            String userIdValue = Integer.toString(userID);
            Document query = new Document(userIdAttribute, userID);
            Document targetWorkoutProgressdocument = null;
            
            MongoCursor<Document> cursor = collection.find(query).iterator();
            
            if(cursor.hasNext()) {

	            // Iterate through the results
	            try {
	            	targetWorkoutProgressdocument = cursor.next();
                    System.out.println(targetWorkoutProgressdocument.toJson());
	                   
	            } finally {
	                cursor.close();
	            }
	            
	            String targetWorkoutProgressAsJson = targetWorkoutProgressdocument.toJson();
            	
            	JSONParser parser = new JSONParser();
    	        JSONObject workoutProgressDocument = (JSONObject)parser.parse(targetWorkoutProgressAsJson);
    	        JSONArray workoutProgressRecords = (JSONArray)workoutProgressDocument.get("userWorkoutRecords");
 
    	        if(workoutProgressRecords != null) {
    	        	 for(int i = 0; i < workoutProgressRecords.size(); i++) {
        	        	 
    	    	           Long tmp;
    	                   JSONObject currentObj = (JSONObject) workoutProgressRecords.get(i);
    	      
    	                   UserWorkoutRecord planRecord = new UserWorkoutRecord();
    	                   //tmp = (Long)currentObj.get("mealPlanID");
    	                   tmp = Long.parseLong(currentObj.get("workoutPlanID").toString());
    	                   planRecord.setWorkoutPlanID(tmp.intValue());
    	                   
    	                   //tmp = (Long)currentObj.get("mealID");
    	                   tmp = Long.parseLong(currentObj.get("workoutID").toString());
    	                   planRecord.setWorkoutID(tmp.intValue());

    	                   //tmp = (Long)currentObj.get("unitsDone");
    	                   tmp = Long.parseLong(currentObj.get("unitsDone").toString());
    	                   planRecord.setUnitsDone(tmp.intValue());

    	                   //tmp = (Long)currentObj.get("userID");
    	                   tmp = Long.parseLong(currentObj.get("userID").toString());
    	                   planRecord.setUserID(tmp.intValue());
    	                   
    	                   planRecord.setDateOfWorkout(currentObj.get("dateOfWorkout").toString());
    	                   
    	                   userWorkoutRecordList.add(planRecord);
    	                   
    	    	       }
    	        }
            }	
            
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		
		
		return userWorkoutRecordList;
	}


	
	public ArrayList<Workout> getWorkoutList() throws Exception {
		
		ArrayList<Workout> workoutList = new ArrayList<Workout>();
		int workoutCount = Integer.parseInt(this.redisConnectionObject.get("LatestWorkoutId"));
		
		for(int i = 1; i <= workoutCount; i++) {
			
			String hashKey = "workout:" + Integer.toString(i);
			
			try {
				Map<String, String> hashData = redisConnectionObject.hgetAll(hashKey);
		
		        if (hashData.isEmpty()) {
		            System.out.println("Hash key '" + hashKey + "' not found in Redis.");
		        } else {
		            System.out.println("Values for hash key '" + hashKey + "':");
		            
		            Workout workout = new Workout();
		            workout.setName(hashData.get("Name"));
		            workout.setWorkoutID(Integer.parseInt(hashData.get("WorkoutID")));
		            workout.setDescription(hashData.get("Description"));
		            workout.setCaloriesBurntPerUnit(Double.parseDouble(hashData.get("CaloriesBurntPerUnit")));
		            
		            workoutList.add(workout);
		        }
			}
			catch (Exception e) {
	    		e.printStackTrace();
	    	}
			
		}
		
		return workoutList;
	}
	
	
	
	public Workout getWorkoutByID(int workoutID) throws Exception{
		
		Workout workout = null;
		
		String hashKey = "workout:" + Integer.toString(workoutID);
		
		try {
			Map<String, String> hashData = redisConnectionObject.hgetAll(hashKey);
	
	        if (hashData.isEmpty()) {
	            System.out.println("Hash key '" + hashKey + "' not found in Redis.");
	        } else {
	            System.out.println("Values for hash key '" + hashKey + "':");
	            
	            workout = new Workout();
	            workout.setName(hashData.get("Name"));
	            workout.setWorkoutID(Integer.parseInt(hashData.get("WorkoutID")));
	            workout.setDescription(hashData.get("Description"));
	            workout.setCaloriesBurntPerUnit(Double.parseDouble(hashData.get("CaloriesBurntPerUnit")));
	            
	        }
		}
		catch (Exception e) {
    		e.printStackTrace();
    	}
		
		
		return workout;
	}
	
	
	
}
