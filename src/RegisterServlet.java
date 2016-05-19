import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public RegisterServlet() {
      
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("In reg");
		JSONObject json = new JSONObject();
		String name,email,pass,mobile,age,bloodGrp;
		name = request.getParameter("n");
		email = request.getParameter("e");
		mobile = request.getParameter("m");
		age = request.getParameter("a");
		bloodGrp = request.getParameter("bl");
		pass = request.getParameter("p");
		
		
		String sqlEmail = "select user_id from userinfo where email=?";
		String sqlMobile = "select user_id from userinfo where phone_no=?";
		
		
		
		String sql = "insert into userinfo(name,age,phone_no,email,blood_group,role,password) values(?,?,?,?,?,'client',?)";
		String sql2 ="select user_id,name from userinfo where email = ?";
		Connection con = ConnectionPool.getConnection();
		try{
			PreparedStatement psEmail = con.prepareStatement(sqlEmail);
			PreparedStatement psMobile = con.prepareStatement(sqlMobile);
			psEmail.setString(1, email);
			psMobile.setString(1, mobile);
			
			ResultSet reEmail = psEmail.executeQuery();
			if(!reEmail.isBeforeFirst()) 
			{
				ResultSet reMobile = psMobile.executeQuery();
				if(!reMobile.isBeforeFirst()){
				
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2,age);
			ps.setString(3, mobile);
			ps.setString(4,email);
			ps.setString(5, bloodGrp);
			ps.setString(6, pass);
			int i = ps.executeUpdate();
			PreparedStatement ps2 = con.prepareStatement(sql2);
			ps2.setString(1, email);
			ResultSet re = ps2.executeQuery();
			if(i>0){
				json.put("info","success");
			}
			else{
				json.put("info","fail");
			}
			if(re.next()){
				json.put("user_id",re.getInt("user_id"));
				json.put("name", re.getString("name"));
			}
			else{
				json.put("user_id",00);
				json.put("name","dummy");
			}
				}
				else{
					json.put("info", "mobilealready");
					json.put("user_id",00);
					json.put("name","dummy");
				}
		}
			else{
				json.put("info", "emailalready");
				json.put("user_id",00);
				json.put("name","dummy");
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
