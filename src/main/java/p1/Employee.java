package p1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import p2.DbUtil;

/**
 * Servlet implementation class Employee
 */
@WebServlet("/Employee")
public class Employee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Employee() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter pw = response.getWriter();
		try {
			
			// Load the Driver 
			Class.forName("com.mysql.cj.jdbc.Driver");
			pw.println("Driver Loaded");
			
			// Establish Connection 
			
			Connection conn = String dbUrl = System.getenv("BANK_DB_URL");
			String dbUser = System.getenv("BANK_DB_USER");
			String dbPassword = System.getenv("BANK_DB_PASSWORD");

			Connection conn = DbUtil.getConnection();
			pw.println("Connection Established");
			
			Statement st = conn.createStatement();
			
			int empId = Integer.parseInt(request.getParameter("empid"));
			String empname = request.getParameter("name");
			String mobileno = request.getParameter("phno");
			String email = request.getParameter("email");
			String position = request.getParameter("position");
			String password = request.getParameter("password");
			
			String query = "insert into employee values("+empId+",'"+empname+"','"+position+"','"+mobileno+"','"+email+"','"+password+"')";
			
			boolean eidExist = false;
			ResultSet check1 = st.executeQuery("select * from employee where empid="+empId);
			if(check1.next())
				eidExist = true;
			boolean emailExist = false;
			ResultSet check2 = st.executeQuery("select * from employee where email='"+email+"'");
			if(check2.next())
				emailExist = true;
			if(eidExist || emailExist)
				response.sendRedirect("AccountExistEmployee.html");
			int i = st.executeUpdate(query);
			if(i > 0)
				response.sendRedirect("EmployeeMainLogin.html");
			else 
				response.sendRedirect("AccountExistEmployee.html");
			// Connection Closed 
			conn.close();
			pw.println("Connection Closed");
			
		}
		catch(Exception e)
		{
			pw.println("Exception Occured");
			pw.println(e.getMessage());
		}
	}

}
