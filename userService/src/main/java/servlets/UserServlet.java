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

import com.example.userService.model.User;
import com.google.gson.Gson;
import com.example.userService.adapters.UserAdapter;
import org.json.simple.parser.JSONParser;

@WebServlet("/api/user/*")
public class UserServlet extends HttpServlet {

	private UserAdapter dao;

	@Override
	public void init() throws ServletException {
		this.dao = new UserAdapter();
		
		try {
			URL obj = new URL("http://localhost:8080/serviceRegistry/api/service/registerService?name=user&portNumber=8083");
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

	// validateUser
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		Gson gson = new Gson();
		// user login
		if (pathInfo.equals("/validateUser")) {

			try {
				String emailID = req.getParameter("email");
				String password = req.getParameter("password");

				if (emailID.isEmpty() || password.isEmpty()) {
					// resp.getWriter().write();
					resp.setStatus(500);
				} else {
					User user = dao.loadUser(emailID, password);

					if (user == null) {
						resp.setStatus(500);
					} else {
						resp.setStatus(200);
						
						resp.setStatus(200);
						PrintWriter out = resp.getWriter();
						resp.setContentType("application/json");
						resp.setCharacterEncoding("UTF-8");

						String resultAsJsonString = gson.toJson(user);
						out.print(resultAsJsonString);
						
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
			}
			
		}
		
		// user registration
		else if (pathInfo.equals("/registerUser")) {

			try {
				String emailID = req.getParameter("email");
				String password = req.getParameter("password");
				String phoneNumber = req.getParameter("phone");
				String name = req.getParameter("name");

				if (emailID.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || name.isEmpty()) {
					// resp.getWriter().write();
					System.out.println("3");
				}

				User newUser = new User();
				newUser.setEmailID(emailID);
				newUser.setPassword(password);
				newUser.setPhoneNumber(phoneNumber);
				newUser.setName(name);
				newUser.setAdmin(0);
				// create new user
				boolean result = dao.createUser(newUser);

				// return result in the response

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
			URL obj = new URL("http://localhost:8080/serviceRegistry/api/service/deregisterService?name=user");
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