import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;


public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

   
    public LoginServlet() {
      
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("In here");
		JSONObject json = new JSONObject();
		Enumeration paraNames = request.getParameterNames();
		String params[] = new String[2];
		int i=0;
		while(paraNames.hasMoreElements()){
			String paramNames = (String) paraNames.nextElement();
			String[] paramValues = request.getParameterValues(paramNames);
			params[i] = paramValues[0];
			i++;
		}
		String sql = "select user_id,role,name from userinfo where email=? and password=?";
		Connection con = ConnectionPool.getConnection();
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, params[0]);
			ps.setString(2,params[1]);
			ResultSet re = ps.executeQuery();
			if(re.next()){
				json.put("info","success");
				json.put("role",re.getString("role"));
				json.put("user_id",re.getInt("user_id"));
				json.put("name", re.getString("name"));
			}
			else{
				json.put("info","fail");
				json.put("user_id",null);
				json.put("role","Invalid" );
				json.put("name", "Invalid");
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
