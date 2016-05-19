

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Location extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 // static map that will tell compatible blood group
	private static Map<String, List<String>> compatibility;
    
	
	@Override
	public void init() throws ServletException {
		super.init();
		compatibility = new HashMap<String,List<String>>();
		
		//O+
		List<String> oPositive = new ArrayList<String>();
		oPositive.add("O+");
		oPositive.add("O-");
		
		compatibility.put("O+", oPositive);
		
		//A+
		List<String> aPositive = new ArrayList<String>();
		aPositive.add("A+");
		aPositive.add("A-");
		aPositive.add("O+");
		aPositive.add("O-");
		
		compatibility.put("A+", aPositive);
		
		//B+
		List<String> bPositive = new ArrayList<String>();
		bPositive.add("B+");
		bPositive.add("B-");
		bPositive.add("O+");
		bPositive.add("O-");
		
		compatibility.put("B+", bPositive);
		
		//AB+
		List<String> abPositive = new ArrayList<String>();
		abPositive.add("A+");
		abPositive.add("A-");
		abPositive.add("B+");
		abPositive.add("B-");
		abPositive.add("O+");
		abPositive.add("O-");
		abPositive.add("AB+");
		abPositive.add("AB-");
		abPositive.add("Bombay");
		
		
		compatibility.put("AB+", abPositive);
		
		//O-
		List<String> oNegative = new ArrayList<String>();
		oNegative.add("O-");
				
		compatibility.put("O-",oNegative);
				
		//A-
		List<String> aNegative = new ArrayList<String>();
		aNegative.add("O-");
		aNegative.add("A-");
		
		compatibility.put("A-",aNegative);
		
		//B-
		List<String> bNegative = new ArrayList<String>();
		bNegative.add("O-");
		bNegative.add("B-");
		
		compatibility.put("B-",bNegative);
		
		//AB-
		List<String> abNegative = new ArrayList<String>();
		abNegative.add("AB-");
		abNegative.add("A-");
		abNegative.add("B-");
		abNegative.add("O-");
		
		compatibility.put("AB-",abNegative);
		
		//Bombay
		List<String> bombay = new ArrayList<String>();
		bombay.add("Bombay");
		
		compatibility.put("Bombay", bombay);
		
	}
    


	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = ConnectionPool.getConnection();
		
		String role;
		role = request.getParameter("role");
		if (role.equals("hosp")){
			findHospital(request, response,con);
		}
		else if(role.equals("don")){
			findDonor(request,response,con);
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void findHospital(HttpServletRequest request, HttpServletResponse response,Connection con)throws ServletException, IOException{
		double lat,lon;
		//in miles
		int distance =4; 
		lat = Double.parseDouble(request.getParameter("lat"));
		lon = Double.parseDouble(request.getParameter("lon"));
		String query = "{CALL geodist(?,?,?)}";
		JSONObject json ;
		JSONArray jArray = new JSONArray();
		try {
			System.out.println(lat);
			System.out.println(lon);
			CallableStatement stmt = con.prepareCall(query);
			stmt.setDouble(1, lat);
			stmt.setDouble(2, lon);
			stmt.setInt(3, distance);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()){ 
				
				json = new JSONObject();
				json.put("distance", rs.getDouble("distance"));
				json.put("srno", rs.getString("hospital_id"));
				json.put("name", rs.getString("hospital_name"));
				json.put("address",rs.getString("address"));
				json.put("latitude",rs.getString("latitude"));
				json.put("longitude",rs.getString("longitude"));
								
				jArray.add(json);
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(jArray);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jArray.toString());
		
	}

	
	protected void findDonor(HttpServletRequest request,HttpServletResponse response,Connection con)throws ServletException,IOException{
		// in url we are getting user ,lat,lon,blood
		double lat,lon;
		String bloodGroup;
		int userid;
		// Donors within in miles
		int distance =10; 
		lat = Double.parseDouble(request.getParameter("lat"));
		lon = Double.parseDouble(request.getParameter("lon"));
		bloodGroup = request.getParameter("blood");
		userid = Integer.parseInt(request.getParameter("user"));
		//parameters are latitude longitude distance
		String query = "{CALL donorDist(?,?,?)}";
		JSONObject json ;
		JSONArray jArray = new JSONArray();
		try {
			System.out.println(lat);
			System.out.println(lon);
			System.out.print(bloodGroup);
			CallableStatement stmt = con.prepareCall(query);
			stmt.setDouble(1, lat);
			stmt.setDouble(2, lon);
			stmt.setInt(3, distance);
			
			//on result set apply criteria for last donated checked against current date
			// &filter based on compatible blood group
			Date currentDate = new Date();
			long diff;
			long day=0;
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){ 
				if(compatibility.get(bloodGroup).contains(rs.getString("blood_group")) && rs.getInt("user_id")!=userid){
				System.out.println("User date:"+rs.getDate("date"));
				
				if(rs.getDate("date")!=null){
				diff = currentDate.getTime() - rs.getDate("date").getTime();
				day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				}
				
				if(day>=56 || rs.getDate("date")==null){
				json = new JSONObject();
				json.put("user_id", rs.getInt("user_id"));
				json.put("name", rs.getString("name"));
				json.put("phone_no", rs.getDouble("phone_no"));
				json.put("blood_group", rs.getString("blood_group"));
				json.put("latitude",rs.getDouble("latitude"));
				json.put("longitude",rs.getDouble("longitude"));
				json.put("distance", rs.getString("distance"));				
				jArray.add(json);
				}
				else{
					System.out.println("Not eligible to donate right now Day count is:"+day);
				}
				}
				else{
					System.out.println("Incompatible blood group not required "+rs.getString("blood_group"));
				}
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(jArray);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jArray.toString());
	}

}
