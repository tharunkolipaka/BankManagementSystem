package p1;
import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserCreateAccount
 */
@WebServlet("/UserCreateAccount")
public class UserCreateAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserCreateAccount() {
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
			// pw.println("Driver loaded");
			
			// Establish Connection 
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BankManagementSystem","root","tiger");
			// pw.println("Connection Established");
			
			// Statement Creation 
			Statement st = conn.createStatement();
			// Inserting into Customer Table 
			// Customer table(cid primary key,name,mobile,email,aadhaar,pan,pin,balance)
			String name = request.getParameter("name");
			long mobile = Long.parseLong(request.getParameter("phno"));
			String email = request.getParameter("email");
			long aadhaar = Long.parseLong(request.getParameter("aadhaar"));
			String pan = request.getParameter("pan");
			int pin = Integer.parseInt(request.getParameter("pin"));
			
			ResultSet rs = st.executeQuery("SELECT COUNT(name) FROM customer");
			int cid = 0;
			if (rs.next()) {
			    cid = 100 + rs.getInt(1);
			} else {
			    pw.println("Error in counting the number of rows in Customer Table");
			}
			int balance = 0;
			
			boolean checkAadhaarExist = false;
			ResultSet check1 = st.executeQuery("select aadhaar from customer where aadhaar="+aadhaar);
			if(check1.next())
				checkAadhaarExist = true;
			boolean checkPanExist = false;
			ResultSet check2 = st.executeQuery("select * from customer where pan='"+pan+"'");
			if(check2.next())
				checkPanExist = true;
			if(checkPanExist || checkAadhaarExist)
				response.sendRedirect("AccountExist.html");
			int i = st.executeUpdate("insert into customer values("+cid+",'"+name+"',"+mobile+",'"+email+"',"+aadhaar+",'"+pan+"',"+pin+","+balance+")");
			if(i > 0)
			{
				// Inserting into Accounts Table where cid is the foriegn key.
				// Accounts(accNo primary key,cid foriegn key,cust_name,balance,opendate)
				pw.println("Came here 1");
				String insertQuery = "insert into accounts values(?,?,?,?,?)";
				PreparedStatement ps = conn.prepareStatement(insertQuery);
				rs = st.executeQuery("SELECT COUNT(name) FROM accounts");
				int accNo = 1000;
				if(rs.next())
				{
					accNo += rs.getInt(1);
				}
				else 
				{
					pw.println("Unable to enter the user data into accounts table");
					response.sendRedirect("MainUserLogin.html");
				}
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = new Date();
			    String curr_date = (String)(formatter.format(date));
				pw.println("Came here 2");
				ps.setInt(1, accNo);
				ps.setInt(2, cid);
				ps.setString(3, name);
				ps.setInt(4,balance);
			    ps.setString(5, curr_date);
				pw.println("Came here 3");
				i = ps.executeUpdate();
				pw.println("Came here 4");
				if(i>0)
				{
					pw.println("Record Inserted Successfully");
					response.sendRedirect("UserLogin.html");
				}
				else 
				{
					pw.println("Record is not inserted Successfully");
					response.sendRedirect("MainUserLogin.html");
				}
			}
			else 
			{
				pw.println("Record is not inserted Successfully");
				response.sendRedirect("MainUserLogin.html");
			}
			// Connection Closed 
			pw.println("Connection Closed");
			conn.close();
			
		}
		catch(Exception e)
		{
			pw.println("Exception Occured");
			pw.println(e.getMessage());
		}
	}

}
