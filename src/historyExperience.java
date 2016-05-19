

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class historyExperience extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String functionality;
	Connection con;
	
	
	con = ConnectionPool.getConnection();
	functionality = request.getParameter("type");
	if(functionality.equals("history")){
		displayHistory(request,response,con);
	}
	else if(functionality.equals("experience")){
		shareExperience(request,response,con);
	}
	
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

	public void shareExperience(HttpServletRequest request, HttpServletResponse response,Connection con) throws ServletException, IOException{
		int userid;
		String experience;
		JSONObject json = new JSONObject();
		java.util.Date d = new java.util.Date();
		java.sql.Date currentDate = new Date(d.getTime());
		
		userid = Integer.parseInt(request.getParameter("uid"));
		experience = request.getParameter("exp");
		String sql = "insert into experience(user_id,experience,date) values (?,?,?)";
		System.out.println("date = "+d);
		System.out.println("userid ="+userid);
		System.out.println("experience = "+experience);
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1,userid);
			ps.setString(2,experience);
			ps.setDate(3, currentDate);
			
			int re = ps.executeUpdate();
			if(re>0){
				json.put("info","success");
			}
			else{
				json.put("info","fail");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		System.out.println(json);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
	}
	
	public void displayHistory(HttpServletRequest request, HttpServletResponse response,Connection con) throws IOException{
		int userid;
		JSONObject json;
		JSONArray jArray = new JSONArray();
		userid = Integer.parseInt(request.getParameter("uid"));
		String sql = "select d.date,h.hospital_name,u.name from donates d,hospital h,userinfo u where d.user_id_donor=? and d.hospital_id=h.hospital_id and u.user_id=d.user_id_client";
		
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, userid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				json = new JSONObject();
				json.put("hospitalName", rs.getString("hospital_name"));
				json.put("name", rs.getString("name"));
				json.put("date", rs.getString("date"));
				jArray.add(json);
			}
			
			
			//ConnectionPool.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(jArray);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jArray.toString());
	}
	
}
