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
 * Servlet implementation class UserFromEmployeeDetails
 */
@WebServlet("/UserFromEmployeeDetails")
public class UserFromEmployeeDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserFromEmployeeDetails() {
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
			// Load the Driver 
			Class.forName("com.mysql.cj.jdbc.Driver");
			// pw.println("Driver Class Loaded");
			
			// Establish the Connection 
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankManagementSystem","root","tiger");
			// pw.println("Connection Established");
			
			Statement st = conn.createStatement();
			// pw.println("Statement Created");
			
			long phno = Long.parseLong(request.getParameter("Phno"));
			
			String query = "select *from customer where mobile="+phno;
			ResultSet rs = st.executeQuery(query);
			response.setContentType("text/html");
			if(rs.next())
			{
				pw.println("<html><body align='center' bgcolor='yellow'>");
				pw.println("<table border='1' align='center'>");
				pw.println("<tr><th>Id</th> <th>Name</th> <th>Mobile</th> <th>Email</th> <th>Aadhaar</th> <th>Pan</th> <th>Pin</th> <th>Balance</th>");
				pw.println("<tr>");
				pw.println("<td>"+rs.getInt(1)+"</td>");
				pw.println("<td>"+rs.getString(2)+"</td>");
				pw.println("<td>"+rs.getLong(3)+"</td>");
				pw.println("<td>"+rs.getString(4)+"</td>");
				pw.println("<td>"+rs.getLong(5)+"</td>");
				pw.println("<td>"+rs.getString(6)+"</td>");
				pw.println("<td>"+"****" +"</td>");
				pw.println("<td>"+rs.getString(8)+"</td>");
				pw.println("</tr>");
				pw.println("</table>");
				pw.println("</body></html>");
			}
			else 
				response.sendRedirect("SorryNotFound.html");
		}
		catch(Exception e)
		{
			pw.println("Exception Occured");
			pw.println(e.getMessage());
		}
	}

}
