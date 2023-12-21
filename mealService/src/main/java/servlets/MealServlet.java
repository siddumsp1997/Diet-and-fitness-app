package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.sql.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.mealService.model.MealPlanRecord;
import com.example.mealService.model.Meal;
import com.example.mealService.model.MealIntakeRecordDetail;
import com.example.mealService.model.MealRecordDetail;
import com.example.mealService.model.UserMealIntakeRecord;
import com.google.gson.Gson;
import com.example.mealService.adapters.MealPlanAdapter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet("/api/meal/*")
public class MealServlet extends HttpServlet {

	private MealPlanAdapter dao;

	@Override
	public void init() throws ServletException {

		this.dao = new MealPlanAdapter();
		
		try {
			URL obj = new URL("http://localhost:8080/serviceRegistry/api/service/registerService?name=meal&portNumber=8082");
		    HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
		    postConnection.setRequestMethod("POST");
	
		    postConnection.setDoOutput(true);
		    OutputStream os = postConnection.getOutputStream();
		    //os.write(POST_PARAMS.getBytes());
		    os.flush();
		    os.close();
	
	
		    int responseCode = postConnection.getResponseCode();
		    System.out.println("Response Code from ServiceRegistry:  " + responseCode);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String pathInfo = req.getPathInfo();
		Gson gson = new Gson();
		// user login
		if (pathInfo.equals("/getMealPlan")) {
			
			
			try {
				
				String userId = req.getParameter("id");

				if (userId.isEmpty()) {
					resp.setStatus(500);
				} else {
					ArrayList<MealPlanRecord> result = new ArrayList<MealPlanRecord>();
					result = dao.getMealPlanRecordsForUser(Integer.parseInt(userId));
					
					resp.setStatus(200);
					PrintWriter out = resp.getWriter();
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");
					
					String resultAsJsonString = gson.toJson(result);

					out.print(resultAsJsonString);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
			}
			
			
		}
		else if (pathInfo.equals("/getMealStatusForUser")) {
			
			
			try {
				
				String userId = req.getParameter("id");

				if (userId.isEmpty()) {
					resp.setStatus(500);
				} else {
					
					ArrayList<MealPlanRecord> mealPlanRecordsOfUser = dao.getMealPlanRecordsForUser(Integer.parseInt(userId));
					
					
					Map<Integer, MealPlanRecord> mealPlanDictionary = new HashMap<Integer, MealPlanRecord>();
					
					for(int i = 0; i < mealPlanRecordsOfUser.size(); i++) {
						mealPlanDictionary.put(mealPlanRecordsOfUser.get(i).getMealID(), mealPlanRecordsOfUser.get(i));
					}
					
					ArrayList<Meal> meals = dao.getMealList();
					ArrayList<MealRecordDetail> result = new ArrayList<MealRecordDetail>();
					
					for(int i = 0; i < meals.size(); i++) {
						
						MealRecordDetail mealRecord = new MealRecordDetail();
						mealRecord.setMealId(meals.get(i).getMealID());
						mealRecord.setName(meals.get(i).getName());
						mealRecord.setDescription(meals.get(i).getDescription());
						

						// if meal opted by user
						if(mealPlanDictionary.containsKey(meals.get(i).getMealID())) {
							
							MealPlanRecord planRecord = mealPlanDictionary.get(meals.get(i).getMealID());
							
							mealRecord.setTargetUnitsPerDay(planRecord.getTargetUnitsPerDay());
							
							mealRecord.setMealPlanId(planRecord.getMealPlanID());
							
							mealRecord.setMealType(planRecord.getMealType());
						}
						else {
							
							mealRecord.setTargetUnitsPerDay(0);
							
							mealRecord.setMealPlanId(-1);
							
							mealRecord.setMealType("None");
						}
						
						result.add(mealRecord);
					}

					resp.setStatus(200);
					PrintWriter out = resp.getWriter();
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");

					String resultAsJsonString = gson.toJson(result);
					out.print(resultAsJsonString);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
			}
			
		}
		else if (pathInfo.equals("/getMealIntake")) {
			
			
			try {
				
				String userId = req.getParameter("id");

				if (userId.isEmpty()) {
					resp.setStatus(500);
				} else {
					ArrayList<UserMealIntakeRecord> result = new ArrayList<UserMealIntakeRecord>();
					result = dao.getUserMealIntakeRecordList(Integer.parseInt(userId));
					
					ArrayList<MealIntakeRecordDetail> mealIntakeRecords = new ArrayList<MealIntakeRecordDetail>(); 
					
					
					for(int i = 0; i < result.size(); i++) {
						
						MealIntakeRecordDetail record = new MealIntakeRecordDetail();
						
						Meal mealRecord = dao.getMealByID(result.get(i).getMealID());
						
						record.dateOfMeal = result.get(i).getDateOfMeal();
						record.mealName = mealRecord.getName();
						record.userID = result.get(i).getUserID();
						record.mealID = result.get(i).getMealID();
						record.mealPlanID = result.get(i).getMealPlanID();
						record.mealType = result.get(i).getMealType();
						record.unitsDone = result.get(i).getUnitsDone();
						
						
						mealIntakeRecords.add(record);
					}
					
					resp.setStatus(200);
					PrintWriter out = resp.getWriter();
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");

					String resultAsJsonString = gson.toJson(mealIntakeRecords);
					out.print(resultAsJsonString);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
			}
			
			
		}
		
	}

	protected String retrieveRequestBody(HttpServletRequest request) {
		
		String requestDataString = "";
		try {
			requestDataString = request.getReader().lines().collect(Collectors.joining());
			return requestDataString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return requestDataString;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		
		if (pathInfo.equals("/updateMeal")){
			
			try {
				
				String requestBodyAsString = retrieveRequestBody(req);
				System.out.println(requestBodyAsString);
				
				JSONParser parser = new JSONParser();

	            JSONObject currentObj = (JSONObject)parser.parse(requestBodyAsString);

	            Long tmp;

                Meal meal = new Meal();
                   
                meal.setName((String)currentObj.get("name"));
                                 
                meal.setCaloriesBurntPerUnit(Double.parseDouble(currentObj.get("caloriesBurntPerUnit").toString()));
                    
                meal.setDescription((String)currentObj.get("description"));
                    
                dao.updateMeal(meal);

	            resp.setStatus(200);
				
			} catch (Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
			}
		}
		else if (pathInfo.equals("/updateMealPlan")) {
			
			try {
				
				String requestBodyAsString = retrieveRequestBody(req);
				System.out.println(requestBodyAsString);
				
				JSONParser parser = new JSONParser();

	            JSONArray requestBody = (JSONArray)parser.parse(requestBodyAsString);
	            
	            ArrayList<MealPlanRecord> planRecords = new ArrayList<MealPlanRecord>();
	            int userId = -1;

	            for(int i = 0; i < requestBody.size(); i++){
	                Long tmp;
	                JSONObject currentObj = (JSONObject) requestBody.get(i);
	                tmp = (Long)currentObj.get("userID");
	                userId = tmp.intValue();
	                tmp = (Long)currentObj.get("targetUnitsPerDay");

	                if(tmp.intValue() != 0){

	                    MealPlanRecord planRecord = new MealPlanRecord();
	                    tmp = (Long)currentObj.get("mealPlanID");
	                    planRecord.setMealPlanID(tmp.intValue());

	                    tmp = (Long)currentObj.get("mealID");
	                    planRecord.setMealID(tmp.intValue());

	                    tmp = (Long)currentObj.get("targetUnitsPerDay");
	                    planRecord.setTargetUnitsPerDay(tmp.intValue());

	                    tmp = (Long)currentObj.get("userID");
	                    planRecord.setUserID(tmp.intValue());

	                    tmp = (Long)currentObj.get("mealID");
	                    Meal currentMeal = dao.getMealByID(tmp.intValue());
	                    
	                    planRecord.setMealType((String)currentObj.get("mealType"));

	                    tmp = (Long)currentObj.get("targetUnitsPerDay");
	                    planRecord.setTargetCalorieCount(tmp.intValue() * currentMeal.getCaloriesBurntPerUnit());

	                    planRecords.add(planRecord);
	                }

	            }

	            dao.updateMealPlanForUser(planRecords, userId);
	            resp.setStatus(200);
				
			} catch (Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
			}
		}
		else if (pathInfo.equals("/updateMealIntake")) {
			
			try {
				
				String requestBodyAsString = retrieveRequestBody(req);
				System.out.println(requestBodyAsString);
				
				JSONParser parser = new JSONParser();

	            JSONArray requestBody = (JSONArray)parser.parse(requestBodyAsString);
	            
	            ArrayList<UserMealIntakeRecord> mealRecords = new ArrayList<UserMealIntakeRecord>();
	            int userId = -1;

	            for(int i = 0; i < requestBody.size(); i++){
	                Long tmp;
	                JSONObject currentObj = (JSONObject) requestBody.get(i);
	                tmp = (Long)currentObj.get("userID");
	                userId = tmp.intValue();
	                tmp = (Long)currentObj.get("unitsDone");

	                if(tmp.intValue() != 0){

	                	UserMealIntakeRecord mealIntakeRecord = new UserMealIntakeRecord();
	                    tmp = (Long)currentObj.get("mealPlanID");
	                    mealIntakeRecord.setMealPlanID(tmp.intValue());

	                    tmp = (Long)currentObj.get("mealID");
	                    mealIntakeRecord.setMealID(tmp.intValue());

	                    tmp = (Long)currentObj.get("unitsDone");
	                    //workoutRecord.setTargetUnitsPerDay(tmp.intValue());
	                    mealIntakeRecord.setUnitsDone(tmp.intValue());

	                    tmp = (Long)currentObj.get("userID");
	                    mealIntakeRecord.setUserID(tmp.intValue());

	                    mealIntakeRecord.setDateOfMeal((String)currentObj.get("dateOfMeal"));
	                    mealIntakeRecord.setMealType((String)currentObj.get("mealType"));
	                    
	                    //dao.addUserMealIntakeRecord(mealIntakeRecord);
	                    //planRecords.add(workoutRecord);
	                    mealRecords.add(mealIntakeRecord);
	                }

	                
	            }
	            
	            if(mealRecords.size() > 0) {
	            	dao.addUserMealIntakeRecord(mealRecords);
	            }
	            
	            resp.setStatus(200);
	            
			} catch (Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
			}
		}

	}
	
	

	@Override
	public void destroy() {
		// Here, you can close your database connection before the servlet is destroyed
		// dbManager.closeConnection(); // You should implement this method in your
		// dbManager to properly release resources
		try {
			URL obj = new URL("http://localhost:8080/serviceRegistry/api/service/deregisterService?name=meal");
		    HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
		    postConnection.setRequestMethod("POST");
	
		    postConnection.setDoOutput(true);
		    OutputStream os = postConnection.getOutputStream();
		    //os.write(POST_PARAMS.getBytes());
		    os.flush();
		    os.close();
	
	
		    int responseCode = postConnection.getResponseCode();
		    System.out.println("Response Code from ServiceRegistry:  " + responseCode);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}