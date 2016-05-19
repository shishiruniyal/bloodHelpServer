

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

public class LocationUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("In here");
		JSONObject json = new JSONObject();
		int user;
		double lat,lon;
		
		user = Integer.parseInt(request.getParameter("user"));
		lat = Double.parseDouble(request.getParameter("lat"));
		lon = Double.parseDouble(request.getParameter("lon"));
		System.out.println(lat+","+lon+" https://www.google.co.in/maps/place/"+lat+"+"+lon);
		
		String sql = "update location set latitude=?,longitude=? where user_id=?";
		
		Connection con = ConnectionPool.getConnection();
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setDouble(1,lat);
			ps.setDouble(2,lon);
			ps.setInt(3, user);
			int re = ps.executeUpdate();
			if(re >0){
				json.put("info", "success");
				json.put("id", ""+user);
			}
			else{
				json.put("info", "fail");
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

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
