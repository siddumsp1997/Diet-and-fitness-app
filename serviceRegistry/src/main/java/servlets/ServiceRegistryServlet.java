package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

import com.google.gson.Gson;
import org.json.simple.parser.JSONParser;
import com.example.serviceRegistry.model.ServiceAddress;

@WebServlet("/api/service/*")
public class ServiceRegistryServlet extends HttpServlet {

	public ConcurrentHashMap<String, Integer> servicesMapping;

	@Override
	public void init() throws ServletException {
		this.servicesMapping = new ConcurrentHashMap<>();
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String pathInfo = req.getPathInfo();
		Gson gson = new Gson();
		// user login
		if (pathInfo.equals("/getServiceAddress")) {

			try {
				
				String serviceName = req.getParameter("name");
				
				if (!servicesMapping.containsKey(serviceName)) {
					resp.setStatus(500);
				} else {
					
					int servicePortNumber = servicesMapping.get(serviceName);
					
					ServiceAddress serviceAddress = new ServiceAddress();
					serviceAddress.setPortNumber(servicePortNumber);
					
					resp.setStatus(200);
					PrintWriter out = resp.getWriter();
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");
					
					String resultAsJsonString = gson.toJson(serviceAddress);
					out.print(resultAsJsonString);
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
			}

		}

	}

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		Gson gson = new Gson();
		// user login
		if (pathInfo.equals("/registerService")) {

			try {
				String serviceName = req.getParameter("name");
				int portNumber = Integer.parseInt(req.getParameter("portNumber"));

				if (serviceName.isEmpty() || portNumber < 0) {
					// resp.getWriter().write();
					resp.setStatus(500);
				} 
				else {
					if(!servicesMapping.containsKey(serviceName)) {
						servicesMapping.put(serviceName, portNumber);
					}
					
					resp.setStatus(200);
				}
				
			} 
			catch (Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
			}
			
		}
		else if (pathInfo.equals("/deregisterService")) {

			try {
				String serviceName = req.getParameter("name");

				if (serviceName.isEmpty()) {
					resp.setStatus(500);
				} 
				else {
					if(servicesMapping.containsKey(serviceName)) {
						servicesMapping.remove(serviceName);
						resp.setStatus(200);
					}
					else {
						resp.setStatus(500);
					}
					
				}
				
			} 
			catch (Exception e) {
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
	}
}
