package com.example.mealService.adapters;

import com.example.mealService.model.User;
import com.example.mealService.model.UserMealIntakeRecord;
import com.google.gson.Gson;
import com.example.mealService.model.Meal;
import com.example.mealService.model.MealIntakeDocument;
import com.example.mealService.model.MealPlan;
import com.example.mealService.model.MealPlanDocument;
import com.example.mealService.model.MealPlanRecord;

import com.mongodb.ServerApi;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClients;
import redis.clients.jedis.Jedis;
import com.mongodb.client.MongoCursor;

import org.bson.Document;
import com.mongodb.client.MongoCollection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;

public class MealPlanAdapter {

	public Jedis redisConnectionObject;
	
	public MongoClient mongoDbConnectionObject;
	
	public MealPlanAdapter() {

		this.redisConnectionObject = null;
        this.mongoDbConnectionObject = null;

        int port = 13901;
        String host = "redis-13901.c8.us-east-1-3.ec2.cloud.redislabs.com";
        //String username = "default";
        String password = "GdSrWOdsZAT7xPKFOet2L1zQxo36jTHb";

        // Create redis connection object using Jedis
        try (Jedis jedis = new Jedis(host, port)) {
            // If your Redis server requires authentication, uncomment the following line and replace with your credentials
            jedis.auth(password);

            System.out.println("Successfully connected to MealServiceDB Redis database...");

            this.redisConnectionObject = jedis;

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        ServerApi serverApiObject = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        String connectionString = "mongodb+srv://siddumsp:siddumsp12345678@mealdb.tdtxvzz.mongodb.net/?retryWrites=true&w=majority";
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApiObject)
                .build();

        try {
            MongoClient mongoClient = MongoClients.create(settings);
            this.mongoDbConnectionObject = mongoClient;
            System.out.println("Successfully connected to MealDB MongoDB database....");
        }
        catch (Exception e) {
        	System.out.println("Not connected to MealDB MongoDB database....");
            e.printStackTrace();
        }
        
	}
	
	
	public int fetchMealPlanId(){
        int result = 0;
        try{
            result = Integer.parseInt(this.redisConnectionObject.get("LatestMealPlanId"));
            //this.redisConnectionObject.set("LatestMealPlanId", Integer.toString(result + 1));
        }
        catch (Exception e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return result + 1;
    }
	
	
	public int fetchMealId(){
		int result = 0;
        try{
            result = Integer.parseInt(this.redisConnectionObject.get("LatestMealId"));
            //this.redisConnectionObject.set("LatestMealId", Integer.toString(result + 1));
        }
        catch (Exception e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return result + 1;
    }
	
	
	public ArrayList<MealPlanRecord> getMealPlanRecordsForUser(int userID) throws Exception{

        ArrayList<MealPlanRecord> mealPlanRecordList = new ArrayList<MealPlanRecord>();
        
        try {
    		
        	 MongoDatabase database = this.mongoDbConnectionObject.getDatabase("mealdb");
             MongoCollection<Document> collection = database.getCollection("mealPlan");

             String userIdAttribute = "userID";
             //String userIdValue = Integer.toString(userID);
             //Document query = new Document(userIdAttribute, userIdValue);
             Document query = new Document(userIdAttribute, userID);
             Document targetMealPlan = null;

             MongoCursor<Document> cursor = collection.find(query).iterator();
             
             
             
             if(cursor.hasNext()) {

	             try {
                	 targetMealPlan = cursor.next();
                     System.out.println(targetMealPlan.toJson());
	             } finally {
	                 cursor.close();
	             }
	             
	             
	             String targetMealPlanAsJson = targetMealPlan.toJson();
	             JSONParser parser = new JSONParser();
		         JSONObject mealPlanDocument = (JSONObject)parser.parse(targetMealPlanAsJson);
		         JSONArray mealPlanRecords = (JSONArray)mealPlanDocument.get("mealPlanRecords");
		         
		         if(mealPlanRecords != null) {
		         
			         for(int i = 0; i < mealPlanRecords.size(); i++) {
			        	 
		        		Long tmp;
		                JSONObject currentObj = (JSONObject) mealPlanRecords.get(i);
		                tmp = Long.parseLong(currentObj.get("targetUnitsPerDay").toString());
		
		                if(tmp.intValue() != 0){
		
		                    MealPlanRecord planRecord = new MealPlanRecord();
		                    //tmp = (Long)currentObj.get("mealPlanId");
		                    tmp = Long.parseLong(currentObj.get("mealPlanID").toString());
		                    planRecord.setMealPlanID(tmp.intValue());
		
		                    //tmp = (Long)currentObj.get("mealId");
		                    tmp = Long.parseLong(currentObj.get("mealID").toString());
		                    planRecord.setMealID(tmp.intValue());
		
		                    //tmp = (Long)currentObj.get("quantity");
		                    tmp = Long.parseLong(currentObj.get("targetUnitsPerDay").toString());
		                    planRecord.setTargetUnitsPerDay(tmp.intValue());
		
		                    //tmp = (Long)currentObj.get("userId");
		                    tmp = Long.parseLong(currentObj.get("userID").toString());
		                    planRecord.setUserID(tmp.intValue());
		
		                    //tmp = (Long)currentObj.get("mealId");
		                    tmp = Long.parseLong(currentObj.get("mealID").toString());
		                    Meal currentMeal = getMealByID(tmp.intValue());
		                    
		                    planRecord.setMealType(currentObj.get("mealType").toString());
		
		                    //tmp = (Long)currentObj.get("quantity");
		                    tmp = Long.parseLong(currentObj.get("targetUnitsPerDay").toString());
		                    planRecord.setTargetCalorieCount(tmp.intValue() * currentMeal.getCaloriesBurntPerUnit());
		
		                    mealPlanRecordList.add(planRecord);
		                  
		                }
			         }
		         }
             }
            
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		

        return mealPlanRecordList;
    }
	
	
	
	public boolean updateMealPlanForUser(ArrayList<MealPlanRecord> updatedRecords, int userId) throws Exception{
		
		boolean result = true;
		try {
			
    		MongoDatabase database = this.mongoDbConnectionObject.getDatabase("mealdb");

            // Access the collection
            MongoCollection<Document> collection = database.getCollection("mealPlan");

    		Map<String, String> hashData = redisConnectionObject.hgetAll("mealPlan:" + Integer.toString(userId));
    		
    		if (hashData.isEmpty()) {
                
    			Map<String, String> fieldUpdates = new HashMap<>();
    			int mealPlanId = fetchMealPlanId();
    			this.redisConnectionObject.set("LatestMealPlanId", Integer.toString(mealPlanId));
    			String currentDate = java.time.LocalDate.now().toString();
    			
                fieldUpdates.put("MealPlanID", Integer.toString(mealPlanId));
                fieldUpdates.put("Name", "Meal Plan");
                fieldUpdates.put("UserID", Integer.toString(userId));
                fieldUpdates.put("DateOfCreation", currentDate);
                
                String updateOperation = redisConnectionObject.hmset("mealPlan:" + Integer.toString(userId), fieldUpdates);
                
                if ("OK".equals(updateOperation)) {
                    System.out.println("Meal Plan added for user ID : " + Integer.toString(userId));
                } else {
                	System.out.println("Failed to add Meal Plan for user ID : " + Integer.toString(userId));
                }
    			
            } else {
                
                // Define the filter to identify the document to be deleted
                String userIdAttribute = "userID";
                //String userIdValue = Integer.toString(userId);
                
                //var filter = Filters.eq(userIdAttribute, userIdValue);
                var filter = Filters.eq(userIdAttribute, userId);

                // Use the deleteOne method to delete a single document
                collection.deleteOne(filter);
            }
    		
    		
    		MealPlanDocument mealPlanDocument = new MealPlanDocument();
    		mealPlanDocument.setUserID(userId);
    		mealPlanDocument.setMealPlanRecords(updatedRecords);
    		
    		Gson gson = new Gson();
    		String documentAsString = gson.toJson(mealPlanDocument);
    		
    		Document documentToBeAdded = Document.parse(documentAsString);
            collection.insertOne(documentToBeAdded);
    		
            
    	} catch (Exception e) {
    		e.printStackTrace();
    		result = false;
    	}
		
		return result;
	}
	
	
	
	public boolean updateMeal(Meal meal) {
    	
		try {
    		
    		ArrayList<Meal> mealList = new ArrayList<Meal>();
    		int mealCount = Integer.parseInt(this.redisConnectionObject.get("LatestMealId"));
    		int isAlreadyExisting = 0;
    		
    		for(int i = 1; i <= mealCount; i++) {
    			
    			String hashKey = "meal:" + Integer.toString(i);
    			
				Map<String, String> hashData = redisConnectionObject.hgetAll(hashKey);
				
				if(hashData.get("Name") != null && hashData.get("Name").equals(meal.getName())) {
					
					isAlreadyExisting = 1;
					
					Map<String, String> fieldUpdates = new HashMap<>();
	    			fieldUpdates.put("MealID", Integer.toString(i));
	    			fieldUpdates.put("Name", meal.getName());
	    			fieldUpdates.put("CaloriePerGram",  Double.toString(meal.getCaloriesBurntPerUnit()));
	    			fieldUpdates.put("Description", meal.getDescription());
	    			
	    			redisConnectionObject.hmset("meal:" + Integer.toString(i), fieldUpdates);
					break;
				}
    			
    		}
    		
    		if(isAlreadyExisting == 0) {
    			int mealID = fetchMealId();
    			this.redisConnectionObject.set("LatestMealId", Integer.toString(mealID));
    			
    			Map<String, String> fieldUpdates = new HashMap<>();
    			fieldUpdates.put("MealID", Integer.toString(mealID));
    			fieldUpdates.put("Name", meal.getName());
    			fieldUpdates.put("CaloriePerGram",  Double.toString(meal.getCaloriesBurntPerUnit()));
    			fieldUpdates.put("Description", meal.getDescription());
    			
    			redisConnectionObject.hmset("meal:" + Integer.toString(mealID), fieldUpdates);
    		}
    		
    		
            return true;
            
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return false;
    }
	

	
	public boolean addUserMealIntakeRecord(ArrayList<UserMealIntakeRecord> userMealIntakeRecordList) throws Exception{
		
		boolean result = true;
		try {
			
    		MongoDatabase database = this.mongoDbConnectionObject.getDatabase("mealdb");

            // Access the collection
            MongoCollection<Document> collection = database.getCollection("mealIntake");
            
            String userIdAttribute = "userID";
            String userIdValue = Integer.toString(userMealIntakeRecordList.get(0).getUserID());
            //var filter = Filters.eq(userIdAttribute, userIdValue);
            var filter = Filters.eq(userIdAttribute, userMealIntakeRecordList.get(0).getUserID());
            
            System.out.println("user ID value");
            System.out.println(userIdValue);
            
            ArrayList<UserMealIntakeRecord> userMealIntakeRecords = getUserMealIntakeRecordList(userMealIntakeRecordList.get(0).getUserID());
            
            //
            System.out.println(userMealIntakeRecords.size());
            
            for(int i = 0; i < userMealIntakeRecordList.size(); i++) {
            	userMealIntakeRecords.add(userMealIntakeRecordList.get(i));
            }

	        //userMealIntakeRecords.add(userMealIntakeRecord);
            System.out.println(userMealIntakeRecords.size());
            
            
            collection.deleteOne(filter);
            
            
    		MealIntakeDocument updatedMealIntakeDocument = new MealIntakeDocument();
    		updatedMealIntakeDocument.setUserID(userMealIntakeRecordList.get(0).getUserID());
    		updatedMealIntakeDocument.setMealIntakeRecords(userMealIntakeRecords);
    		
    		Gson gson = new Gson();
    		String documentAsString = gson.toJson(updatedMealIntakeDocument);
    		
    		Document documentToBeAdded = Document.parse(documentAsString);
            collection.insertOne(documentToBeAdded);
           
            
            /*
            Gson gson = new Gson();
            String mealIntakeRecordsAsString = gson.toJson(userMealIntakeRecords);
            System.out.println(mealIntakeRecordsAsString);
            
            collection.updateOne(
                    Filters.eq(userIdAttribute, userIdValue),
                    Updates.set("mealIntakeRecords", mealIntakeRecordsAsString)
            );
            */
    		
            
    	} catch (Exception e) {
    		e.printStackTrace();
    		result = false;
    	}
		
		return result;
	}
	
	
	
	public ArrayList<UserMealIntakeRecord> getUserMealIntakeRecordList(int userID) throws Exception{
		
		ArrayList<UserMealIntakeRecord> userMealIntakeRecordList = new ArrayList<UserMealIntakeRecord>();
		
		try {
			
    		MongoDatabase database = this.mongoDbConnectionObject.getDatabase("mealdb");

            // Access the collection
            MongoCollection<Document> collection = database.getCollection("mealIntake");
            
            String userIdAttribute = "userID";
            String userIdValue = Integer.toString(userID);
            //Document query = new Document(userIdAttribute, userIdValue);
            Document query = new Document(userIdAttribute, userID);
            Document targetMealIntakedocument = null;
            
            MongoCursor<Document> cursor = collection.find(query).iterator();
            
            if(cursor.hasNext()) {

	            // Iterate through the results
	            try {
                	targetMealIntakedocument = cursor.next();
                    System.out.println(targetMealIntakedocument.toJson());
	                   
	            } finally {
	                cursor.close();
	            }
	            
	            String targetMealPlanAsJson = targetMealIntakedocument.toJson();
            	
            	JSONParser parser = new JSONParser();
    	        JSONObject mealIntakeDocument = (JSONObject)parser.parse(targetMealPlanAsJson);
    	        JSONArray mealIntakeRecords = (JSONArray)mealIntakeDocument.get("mealIntakeRecords");
 
    	        if(mealIntakeRecords != null) {
    	        
	    	        for(int i = 0; i < mealIntakeRecords.size(); i++) {
	    	        	 
	    	           Long tmp;
	                   JSONObject currentObj = (JSONObject) mealIntakeRecords.get(i);
	      
	                   UserMealIntakeRecord planRecord = new UserMealIntakeRecord();
	                   //tmp = (Long)currentObj.get("mealPlanID");
	                   tmp = Long.parseLong(currentObj.get("mealPlanID").toString());
	                   planRecord.setMealPlanID(tmp.intValue());
	                   
	                   //tmp = (Long)currentObj.get("mealID");
	                   tmp = Long.parseLong(currentObj.get("mealID").toString());
	                   planRecord.setMealID(tmp.intValue());
	
	                   //tmp = (Long)currentObj.get("unitsDone");
	                   tmp = Long.parseLong(currentObj.get("unitsDone").toString());
	                   planRecord.setUnitsDone(tmp.intValue());
	
	                   //tmp = (Long)currentObj.get("userID");
	                   tmp = Long.parseLong(currentObj.get("userID").toString());
	                   planRecord.setUserID(tmp.intValue());
	                   
	                   planRecord.setDateOfMeal(currentObj.get("dateOfMeal").toString());
	                   
	                   planRecord.setMealType(currentObj.get("mealType").toString());
	
	
	                   userMealIntakeRecordList.add(planRecord);
	                   
	    	         }
    	        }
            	
            }	
            
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		
		
		return userMealIntakeRecordList;
	}
	
	
	
	public ArrayList<Meal> getMealList() throws Exception {
		
		ArrayList<Meal> mealList = new ArrayList<Meal>();
		int mealCount = Integer.parseInt(this.redisConnectionObject.get("LatestMealId"));
		
		for(int i = 1; i <= mealCount; i++) {
			
			String hashKey = "meal:" + Integer.toString(i);
			
			try {
				Map<String, String> hashData = redisConnectionObject.hgetAll(hashKey);
		
		        if (hashData.isEmpty()) {
		            System.out.println("Hash key '" + hashKey + "' not found in Redis.");
		        } else {
		            System.out.println("Values for hash key '" + hashKey + "':");
		            
		            Meal meal = new Meal();
		            meal.setName(hashData.get("Name"));
		            meal.setMealID(Integer.parseInt(hashData.get("MealID")));
		            meal.setDescription(hashData.get("Description"));
		            meal.setCaloriesBurntPerUnit(Double.parseDouble(hashData.get("CaloriePerGram")));
		            
		            mealList.add(meal);
		        }
			}
			catch (Exception e) {
	    		e.printStackTrace();
	    	}
			
		}
		
		
		return mealList;
	}
	

	
	public Meal getMealByID(int mealID) throws Exception{
		
		Meal meal = null;
		
		String hashKey = "meal:" + Integer.toString(mealID);
		
		try {
			Map<String, String> hashData = redisConnectionObject.hgetAll(hashKey);
	
	        if (hashData.isEmpty()) {
	            System.out.println("Hash key '" + hashKey + "' not found in Redis.");
	        } else {
	            System.out.println("Values for hash key '" + hashKey + "':");
	            
	            meal = new Meal();
	            meal.setName(hashData.get("Name"));
	            meal.setMealID(Integer.parseInt(hashData.get("MealID")));
	            meal.setDescription(hashData.get("Description"));
	            meal.setCaloriesBurntPerUnit(Double.parseDouble(hashData.get("CaloriePerGram")));
	            
	        }
		}
		catch (Exception e) {
    		e.printStackTrace();
    	}
		
		
		return meal;
	}
	
}
