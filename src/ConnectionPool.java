
import java.sql.*;

public class ConnectionPool {
//	final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//	final static String DB_URL = "jdbc:mysql://localhost/bloodHelp";
//	final static String USER = "root";
//	final static String PASS = "password";
	
	final static String USER = "adminJ3DCsgg";
	final static String PASS = "n4Q7L9R3LAGk";
	
	static Connection con;
	static String s = "jdbc:mysql://" + "127.2.155.2" + ":" + "3306" + "/" + "bloodHelp?autoReconnect=true";
	static Connection getConnection() {
		if (con == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(s, USER, PASS);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return con;
	}

	static void closeConnection() {
		try {
			if (con != null)
				con.close();
			System.out.println("Connection Closed Successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
