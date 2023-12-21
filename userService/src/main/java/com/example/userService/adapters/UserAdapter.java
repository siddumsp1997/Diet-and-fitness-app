package com.example.userService.adapters;

import com.example.userService.model.User;

import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAdapter {
	
	public Jedis redisConnectionObject;
	
	public UserAdapter() {

		int port = 12523;
        String host = "redis-12523.c324.us-east-1-3.ec2.cloud.redislabs.com";
        //String username = "default";
        String password = "kcVyd8McAclJ39nlKrmPxtcjckoRo1Z9";

        // Create redis connection object using Jedis
        try (Jedis jedis = new Jedis(host, port)) {
            // If your Redis server requires authentication, uncomment the following line and replace with your credentials
            jedis.auth(password);

            System.out.println("Successfully connected to UserServiceDB Redis database...");

            this.redisConnectionObject = jedis;

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
	}
	
	
	public int fetchUserId(){
		int result = 0;
        try{
            result = Integer.parseInt(this.redisConnectionObject.get("LatestUserId"));
            //this.redisConnectionObject.set("LatestMealPlanId", Integer.toString(result + 1));
        }
        catch (Exception e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return result + 1;
    }
    
    
    
    public boolean createUser(User user) {

    	try {
    		
    		ArrayList<User> userList = new ArrayList<User>();
    		int userCount = Integer.parseInt(this.redisConnectionObject.get("LatestUserId"));
    		int isAlreadyExisting = 0;
    		
    		for(int i = 1; i <= userCount; i++) {
    			
    			String hashKey = "user:" + user.getEmailID();
    			
				Map<String, String> hashData = redisConnectionObject.hgetAll(hashKey);
				
				if(hashData != null && hashData.get("EmailID") != null && hashData.get("EmailID").equals(user.getEmailID())) {
					
					isAlreadyExisting = 1;
					
					Map<String, String> fieldUpdates = new HashMap<>();
	    			fieldUpdates.put("UserID", Integer.toString(user.getUserID()));
	    			fieldUpdates.put("Name", user.getName());
	    			fieldUpdates.put("Password", user.getPassword());
	    			fieldUpdates.put("PhoneNumber", user.getPhoneNumber());
	    			fieldUpdates.put("IsAdmin", Integer.toString(0));
	    			fieldUpdates.put("EmailID", user.getEmailID());

	    			
	    			redisConnectionObject.hmset("user:" + user.getEmailID(), fieldUpdates);
					break;
				}
    			
    		}
    		
    		if(isAlreadyExisting == 0) {
    			int userID = fetchUserId();
    			this.redisConnectionObject.set("LatestUserId", Integer.toString(userID));
    			
    			Map<String, String> fieldUpdates = new HashMap<>();
    			fieldUpdates.put("UserID", Integer.toString(userID));
    			fieldUpdates.put("Name", user.getName());
    			fieldUpdates.put("Password", user.getPassword());
    			fieldUpdates.put("PhoneNumber", user.getPhoneNumber());
    			fieldUpdates.put("IsAdmin", Integer.toString(0));
    			fieldUpdates.put("EmailID", user.getEmailID());

    			
    			redisConnectionObject.hmset("user:" + user.getEmailID(), fieldUpdates);
    		}
    		
    		
            return true;
            
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return false;
    }
    
    
    

    public User loadUser(String emailID, String password) {
        try {
        	ArrayList<User> userList = new ArrayList<User>();
    		int userCount = Integer.parseInt(this.redisConnectionObject.get("LatestUserId"));
    		User targetUser = null;
    		
    		for(int i = 1; i <= userCount; i++) {
    			
    			String hashKey = "user:" + emailID;
    			
				Map<String, String> hashData = redisConnectionObject.hgetAll(hashKey);
				
				if(hashData != null && hashData.get("EmailID") != null && hashData.get("EmailID").equals(emailID)) {

					if(hashData.get("Password").equals(password)) {
					
						targetUser = new User();
						
						targetUser.setAdmin(Integer.parseInt(hashData.get("IsAdmin")));
						targetUser.setEmailID(emailID);
						targetUser.setUserID(Integer.parseInt(hashData.get("UserID")));
						
						targetUser.setPassword(hashData.get("Password"));
						targetUser.setPhoneNumber(hashData.get("PhoneNumber"));
						targetUser.setName(hashData.get("Name"));
					}

					break;
				}
    			
    		}
    		
    		
    		return targetUser;

        } catch (Exception e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    
    public boolean updateUser(User user) {
    	try {
    		
    		ArrayList<User> userList = new ArrayList<User>();
    		int userCount = Integer.parseInt(this.redisConnectionObject.get("LatestUserId"));
    		int isAlreadyExisting = 0;
    		
    		for(int i = 1; i <= userCount; i++) {
    			
    			String hashKey = "user:" + user.getEmailID();
    			
				Map<String, String> hashData = redisConnectionObject.hgetAll(hashKey);
				
				if(hashData != null && hashData.get("EmailID") != null && hashData.get("EmailID").equals(user.getEmailID())) {
					
					isAlreadyExisting = 1;
					
					Map<String, String> fieldUpdates = new HashMap<>();
	    			fieldUpdates.put("UserID", Integer.toString(user.getUserID()));
	    			fieldUpdates.put("Name", user.getName());
	    			fieldUpdates.put("Password", user.getPassword());
	    			fieldUpdates.put("PhoneNumber", user.getPhoneNumber());
	    			fieldUpdates.put("IsAdmin", Integer.toString(0));
	    			fieldUpdates.put("EmailID", user.getEmailID());

	    			
	    			redisConnectionObject.hmset("user:" + user.getEmailID(), fieldUpdates);
					break;
				}
    			
    		}
    		
    		if(isAlreadyExisting == 0) {
    			int userID = fetchUserId();
    			this.redisConnectionObject.set("LatestUserId", Integer.toString(userID));
    			
    			Map<String, String> fieldUpdates = new HashMap<>();
    			fieldUpdates.put("UserID", Integer.toString(userID));
    			fieldUpdates.put("Name", user.getName());
    			fieldUpdates.put("Password", user.getPassword());
    			fieldUpdates.put("PhoneNumber", user.getPhoneNumber());
    			fieldUpdates.put("IsAdmin", Integer.toString(0));
    			fieldUpdates.put("EmailID", user.getEmailID());

    			
    			redisConnectionObject.hmset("user:" + user.getEmailID(), fieldUpdates);
    		}
    		
    		
            return true;
            
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return false;
    }
    
    
    
    public boolean deleteUser(User user) {
		try {
			
			String hashKey = "user:" + user.getEmailID();
			
			Long isDeleted = redisConnectionObject.del(hashKey);
			
			if(isDeleted > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
    	return false;
    }
}
