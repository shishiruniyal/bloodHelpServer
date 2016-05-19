

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

@WebServlet("/LifeCount")
public class LifeCount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int donor_userid,client_userid,hospital_id;
		JSONObject json = new JSONObject();
		java.util.Date d = new java.util.Date();
		java.sql.Date currentDate = new Date(d.getTime());
		
		donor_userid = Integer.parseInt(request.getParameter("Duid"));
		client_userid = Integer.parseInt(request.getParameter("Cuid"));
		hospital_id = Integer.parseInt(request.getParameter("Hid"));
		
		String sql = "insert into donates(user_id_donor,user_id_client,date,hospital_id) values (?,?,?,?)";
		Connection con = ConnectionPool.getConnection();
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1,donor_userid);
			ps.setInt(2,client_userid);
			ps.setDate(3,currentDate);
			ps.setInt(4,hospital_id);
			
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
