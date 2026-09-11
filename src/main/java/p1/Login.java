package p1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter pw = response.getWriter();
		
		try
		{
			
			// Load Driver 
			Class.forName("com.mysql.cj.jdbc.Driver");
			pw.println("Driver Loaded");
			
			// Establish Connection 
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankManagementSystem","root","tiger");
			pw.println("Connection Established");
			
			Statement st = conn.createStatement();
			int id = Integer.parseInt(request.getParameter("ID"));
			String pwd = request.getParameter("pwd");
//			String type = request.getParameter("Login");
//			pw.println(type);
			ResultSet rs = st.executeQuery("select empid from employee where empid="+id+" and password="+pwd);
			if(rs.next())
				response.sendRedirect("StaffEnquiryPortal.html");
			else 
			{
				// pw.println(type);
				response.sendRedirect("WrongCredentials.html");
			}
			// Closing the connection 
			conn.close();
			pw.println("Connection closed");
		}
		catch(Exception e)
		{
			pw.println("Exeception Occured");
			pw.println(e.getMessage());
		}
	}

}
