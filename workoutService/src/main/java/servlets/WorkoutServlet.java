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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.workoutService.model.WorkoutPlanRecord;
import com.example.workoutService.model.WorkoutProgressRecordDetail;
import com.example.workoutService.model.Workout;
import com.example.workoutService.model.WorkoutRecordDetail;
import com.example.workoutService.model.UserWorkoutRecord;
import com.google.gson.Gson;
import com.example.workoutService.adapters.WorkoutPlanAdapter;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@WebServlet("/api/workout/*")
public class WorkoutServlet extends HttpServlet {

	private WorkoutPlanAdapter dao;

	@Override
	public void init() throws ServletException {

		this.dao = new WorkoutPlanAdapter();
		
		
		try {
			URL obj = new URL("http://localhost:8080/serviceRegistry/api/service/registerService?name=workout&portNumber=8081");
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
		if (pathInfo.equals("/getWorkoutPlan")) {
			
			
			try {
				
				String userId = req.getParameter("id");

				if (userId.isEmpty()) {
					resp.setStatus(500);
				} else {
					ArrayList<WorkoutPlanRecord> result = new ArrayList<WorkoutPlanRecord>();
					result = dao.getWorkoutPlanRecordsForUser(Integer.parseInt(userId));
					
					resp.setStatus(200);
					PrintWriter out = resp.getWriter();
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");
					
					//String jsonA = JSONArray.toJSONString(result);
					
					String resultAsJsonString = gson.toJson(result);
					//System.out.println("Stttt");
					//System.out.println("resultAsJsonString");
					
					//JSONArray resultAsJsonArray = new JSONArray(resultAsJsonString);
					out.print(resultAsJsonString);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
			}
			
			
		}
		else if (pathInfo.equals("/getWorkoutStatusForUser")) {
			
			
			try {
				
				String userId = req.getParameter("id");

				if (userId.isEmpty()) {
					resp.setStatus(500);
				} else {
					
					ArrayList<WorkoutPlanRecord> workoutPlanRecordsOfUser = dao.getWorkoutPlanRecordsForUser(Integer.parseInt(userId));
					
					
					Map<Integer, WorkoutPlanRecord> workoutPlanDictionary = new HashMap<Integer, WorkoutPlanRecord>();
					
					for(int i = 0; i < workoutPlanRecordsOfUser.size(); i++) {
						workoutPlanDictionary.put(workoutPlanRecordsOfUser.get(i).getWorkoutID(), workoutPlanRecordsOfUser.get(i));
					}
					
					ArrayList<Workout> workouts = dao.getWorkoutList();
					ArrayList<WorkoutRecordDetail> result = new ArrayList<WorkoutRecordDetail>();
					
					for(int i = 0; i < workouts.size(); i++) {
						
						WorkoutRecordDetail workoutRecord = new WorkoutRecordDetail();
						workoutRecord.setWorkoutId(workouts.get(i).getWorkoutID());
						workoutRecord.setName(workouts.get(i).getName());
						workoutRecord.setDescription(workouts.get(i).getDescription());

						// if workout opted by user
						if(workoutPlanDictionary.containsKey(workouts.get(i).getWorkoutID())) {
							
							WorkoutPlanRecord planRecord = workoutPlanDictionary.get(workouts.get(i).getWorkoutID());
							
							workoutRecord.setTargetUnitsPerDay(planRecord.getTargetUnitsPerDay());
							workoutRecord.setWorkoutPlanId(planRecord.getWorkoutPlanID());
						}
						else {
							workoutRecord.setTargetUnitsPerDay(0);
							workoutRecord.setWorkoutPlanId(-1);
						}
						
						result.add(workoutRecord);
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
		else if (pathInfo.equals("/getWorkoutProgress")) {
			
			try {
				
				String userId = req.getParameter("id");

				if (userId.isEmpty()) {
					resp.setStatus(500);
				} else {
					ArrayList<UserWorkoutRecord> result = new ArrayList<UserWorkoutRecord>();
					result = dao.getUserWorkoutRecordList(Integer.parseInt(userId));
					ArrayList<WorkoutProgressRecordDetail> workoutProgressRecords = new ArrayList<WorkoutProgressRecordDetail>(); 
					
					
					for(int i = 0; i < result.size(); i++) {
						
						WorkoutProgressRecordDetail record = new WorkoutProgressRecordDetail();
						
						Workout workoutRecord = dao.getWorkoutByID(result.get(i).getWorkoutID());
						
						record.setDateOfWorkout(result.get(i).getDateOfWorkout());
						record.setName(workoutRecord.getName());
						record.setNoOfUnitsPerformed(result.get(i).getUnitsDone());
						record.setWorkoutId(result.get(i).getWorkoutID());
						record.setWorkoutPlanId(result.get(i).getWorkoutPlanID());

						
						workoutProgressRecords.add(record);
					}
					
					
					
					resp.setStatus(200);
					PrintWriter out = resp.getWriter();
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");

					String resultAsJsonString = gson.toJson(workoutProgressRecords);
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
		Gson gson = new Gson();
		
		if (pathInfo.equals("/updateWorkout")){
			
			try {
				
				String requestBodyAsString = retrieveRequestBody(req);
				System.out.println(requestBodyAsString);
				
				JSONParser parser = new JSONParser();

	            JSONObject currentObj = (JSONObject)parser.parse(requestBodyAsString);

	            Long tmp;
	            
                Workout workout = new Workout();
                    
                workout.setName((String)currentObj.get("name"));
                    
                workout.setCaloriesBurntPerUnit(Double.parseDouble(currentObj.get("caloriesBurntPerUnit").toString()));
                   
                workout.setDescription((String)currentObj.get("description"));
                    
                dao.updateWorkout(workout);

	            resp.setStatus(200);
				
			} catch (Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
			}
		}
		else if (pathInfo.equals("/updateWorkoutPlan")) {
			
			try {
				
				String requestBodyAsString = retrieveRequestBody(req);
				System.out.println(requestBodyAsString);
				
				JSONParser parser = new JSONParser();

	            JSONArray requestBody = (JSONArray)parser.parse(requestBodyAsString);
	            
	            ArrayList<WorkoutPlanRecord> planRecords = new ArrayList<WorkoutPlanRecord>();
	            int userId = -1;

	            for(int i = 0; i < requestBody.size(); i++){
	                Long tmp;
	                JSONObject currentObj = (JSONObject) requestBody.get(i);
	                tmp = (Long)currentObj.get("userID");
	                userId = tmp.intValue();
	                tmp = (Long)currentObj.get("targetUnitsPerDay");

	                if(tmp.intValue() != 0){

	                    WorkoutPlanRecord planRecord = new WorkoutPlanRecord();
	                    tmp = (Long)currentObj.get("workoutPlanID");
	                    planRecord.setWorkoutPlanID(tmp.intValue());

	                    tmp = (Long)currentObj.get("workoutID");
	                    planRecord.setWorkoutID(tmp.intValue());

	                    tmp = (Long)currentObj.get("targetUnitsPerDay");
	                    planRecord.setTargetUnitsPerDay(tmp.intValue());

	                    tmp = (Long)currentObj.get("userID");
	                    planRecord.setUserID(tmp.intValue());

	                    tmp = (Long)currentObj.get("workoutID");
	                    Workout currentWorkout = dao.getWorkoutByID(tmp.intValue());

	                    tmp = (Long)currentObj.get("targetUnitsPerDay");
	                    planRecord.setTargetCalorieCount(tmp.intValue() * currentWorkout.getCaloriesBurntPerUnit());

	                    planRecords.add(planRecord);
	                }

	            }

	            dao.updateWorkoutPlanForUser(planRecords, userId);
	            resp.setStatus(200);
				
			} catch (Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
			}
		}
		else if (pathInfo.equals("/updateWorkoutProgress")) {
			
			try {
				
				String requestBodyAsString = retrieveRequestBody(req);
				System.out.println(requestBodyAsString);
				
				JSONParser parser = new JSONParser();

	            JSONArray requestBody = (JSONArray)parser.parse(requestBodyAsString);
	            
	            ArrayList<UserWorkoutRecord> workoutRecords = new ArrayList<UserWorkoutRecord>();
	            int userId = -1;

	            for(int i = 0; i < requestBody.size(); i++){
	                Long tmp;
	                JSONObject currentObj = (JSONObject) requestBody.get(i);
	                tmp = (Long)currentObj.get("userID");
	                userId = tmp.intValue();
	                tmp = (Long)currentObj.get("unitsDone");

	                if(tmp.intValue() != 0){

	                	UserWorkoutRecord workoutRecord = new UserWorkoutRecord();

	                    tmp = (Long)currentObj.get("workoutID");
	                    workoutRecord.setWorkoutID(tmp.intValue());

	                    tmp = (Long)currentObj.get("unitsDone");
	                    //workoutRecord.setTargetUnitsPerDay(tmp.intValue());
	                    workoutRecord.setUnitsDone(tmp.intValue());

	                    tmp = (Long)currentObj.get("userID");
	                    workoutRecord.setUserID(tmp.intValue());

	                    workoutRecord.setDateOfWorkout((String)currentObj.get("dateOfWorkout"));
	                    
	                    
	                    //planRecords.add(workoutRecord);
	                    workoutRecords.add(workoutRecord);
	                }
	            }
                dao.addUserWorkoutRecord(workoutRecords);
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
				URL obj = new URL("http://localhost:8080/serviceRegistry/api/service/deregisterService?name=workout");
			    HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			    postConnection.setRequestMethod("POST");
			    //postConnection.setRequestProperty("Content-Type", "application/json");
	
	
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