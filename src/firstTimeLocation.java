

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;


public class firstTimeLocation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  		double latitude,longitude;
  		int userid;
  		
  		System.out.println("In here");
		JSONObject json = new JSONObject();
  		
  		latitude = Double.parseDouble(request.getParameter("lat"));
  		longitude = Double.parseDouble(request.getParameter("lon"));
  		userid = Integer.parseInt(request.getParameter("uid"));
  		
  		Connection con = ConnectionPool.getConnection();
  		String sqlMain = "update userinfo set role=? where user_id=?";
  		String sql = "insert into location(user_id,latitude,longitude) values(?,?,?)";
  		try{
			
  			PreparedStatement ps = con.prepareStatement(sql);
			
			PreparedStatement ps1 = con.prepareStatement(sqlMain);
			
			ps1.setString(1, "Donor");
			ps1.setInt(2, userid);
			
			ps.setDouble(2,latitude);
			ps.setDouble(3,longitude);
			ps.setInt(1, userid);
			
			int re = ps.executeUpdate();
			int rs = ps1.executeUpdate();
			
			if(re >0 && rs>0){
				json.put("info", "success");
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
