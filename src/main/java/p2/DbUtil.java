package p2;
import java.sql.*;
public class DbUtil
{
	public static Connection conn;
	public static Statement st;
	public static void connect()
	{
		try {
			// Load the Driver;
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Establishing Connection 
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankManagementSystem","root","tiger");
			st = conn.createStatement();
		}
		catch(Exception e)
		{
			System.out.println("Exception");
		}
	}
}
