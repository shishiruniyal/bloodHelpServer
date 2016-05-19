

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;


public class switchToClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  		//double latitude,longitude;
  		int userid;
  		
  		System.out.println("In here");
		JSONObject json = new JSONObject();
  		
  		userid = Integer.parseInt(request.getParameter("uid"));
  		
  		Connection con = ConnectionPool.getConnection();
  		String sqlMain = "update userinfo set role=? where user_id=?";
  		
  		try{
  			
			PreparedStatement ps1 = con.prepareStatement(sqlMain);
			
			ps1.setString(1, "Client");
			ps1.setInt(2, userid);
			
			int rs = ps1.executeUpdate();
			
			if(rs>0){
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
