package p1;
import java.text.SimpleDateFormat;  
import java.util.Date; 
import p2.DbUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserAccountLogin
 */
@WebServlet("/UserAccountLogin")
public class UserAccountLogin extends HttpServlet {
	static int cid = 0;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserAccountLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter pw = response.getWriter();

		String choice = request.getParameter("type");
		switch(choice)
		{
		case "Login" : {
			callLogin(request,response);
			pw.println(cid);
		};break;
		case "Deposit" : {
			callDeposit(request,response,this.cid,choice);
		};break;
		case "WithDrawl" :{
			callWithDrawl(request,response,this.cid,choice);
		};break;
		case "FundTransfer" : {
			callFundTransfer(request,response,this.cid,choice);
		};break;
		case "GetBalance" : {
			getBalance(request,response,this.cid);
		};break;
		case "ShowUserDetails" : {
			showDetails(request,response);
		};break;
		case "OK" : {
			callTransactions(request,response,this.cid);
		};break;
		default : {
			response.getWriter().write("Default");
		}
		}
	}
	public void callTransactions(HttpServletRequest request, HttpServletResponse response, int cid2) throws IOException,ServletException {
		// TODO Auto-generated method stub
		PrintWriter pw = response.getWriter();
		try 
		{
			ResultSet rs = DbUtil.st.executeQuery("select accNo from accounts where cid="+cid);
			int accNo = 0;
			if(rs.next())
				accNo = rs.getInt(1);
			else 
				response.getWriter().write("The data is not found");
			rs = DbUtil.st.executeQuery("select * from transactions where accNo="+accNo);
			response.setContentType("text/html");
			pw.println("<html><body align='center' bgcolor='yellow'>");
			pw.println("<table border='1' align='center'>");
			pw.println("<tr><th>Account Number</th> <th>Transaction Type</th> <th>Amount </th> <th> Time </th> <th> Transaction ID </th> <th> Recepient Account Number </th> </tr>");
			while(rs.next())
			{
				pw.println("<tr>");
				pw.println("<td>"+rs.getInt(1)+"</td>");
				pw.println("<td>"+rs.getString(2)+"</td>");
				pw.println("<td>"+rs.getInt(3)+"</td>");
				pw.println("<td>"+rs.getString(4)+"</td>");
				pw.println("<td>"+rs.getInt(5)+"</td>");
				pw.println("<td>"+rs.getInt(6)+"</td>");
				pw.println("</tr>");
			}
			pw.println("</table>");
			pw.println("</body></html>");
		}
		catch(Exception e)
		{
			pw.println("Exception Occured");
			pw.println(e.getMessage());
		}
	}

	private void getBalance(HttpServletRequest request, HttpServletResponse response, int cid2) {
		// TODO Auto-generated method stub
		try {
			ResultSet rs = DbUtil.st.executeQuery("select balance from customer where cid="+cid);
			int balance = 0;
			if(rs.next())
				balance = rs.getInt(1);
			else 
				response.getWriter().write("The data is not found");
			request.setAttribute("balance", balance);
			RequestDispatcher dispatcher = request.getRequestDispatcher("CurrentBalance.jsp");
			dispatcher.forward(request,response);
		}
		catch(Exception e)
		{
			
		}
	}
	public void showDetails(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException 
	{
		PrintWriter pw = response.getWriter();
		try {
			// Balance
			ResultSet rs = DbUtil.st.executeQuery("select * from customer where cid="+cid);
			int balance = 0,custoId=0,pin=0;
			long mobile=0,aadhaar=0;
			String pan="",name="",email="";
			int accNo = 0;
			if(rs.next())
			{
				custoId = rs.getInt(1);
				name = rs.getString(2);
				mobile = rs.getLong(3);
				email = rs.getString(4);
				aadhaar = rs.getLong(5);
				pan = rs.getString(6);
				pin = rs.getInt(7);
				balance = rs.getInt(8);
			}
			else 
			{
				response.getWriter().write("Exception Occured");
			}
			rs = DbUtil.st.executeQuery("select accNo from accounts where cid="+cid);
			if(rs.next())
			{
				accNo = rs.getInt(1);
			}
			request.setAttribute("cid",custoId);
			request.setAttribute("name",name);
			request.setAttribute("mobile", mobile);
			request.setAttribute("email", email);
			request.setAttribute("aadhaar", aadhaar);
			request.setAttribute("pan",pan);
			request.setAttribute("pin",pin);
			request.setAttribute("balance", balance);
			request.setAttribute("accNo", accNo);
			RequestDispatcher dispatcher = request.getRequestDispatcher("UserDetails.jsp");
			dispatcher.forward(request,response);
		}
		catch(Exception e)
		{
			pw.println("Exception Occured");
			pw.println(e.getMessage());
		}
	}
	public void callLogin(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	{
		PrintWriter pw = response.getWriter();
		pw.println("Came Here 0");
		try {
			DbUtil.connect();
			long mobileNo = Long.parseLong(request.getParameter("mobile"));
			
			String query = "select pin from customer where mobile = "+mobileNo;
			ResultSet rs = DbUtil.st.executeQuery(query);
			pw.println("Came Here 1");
			int original_pin = 0;
			if(rs.next())
			{
				original_pin = rs.getInt(1);
			}
			else 
			{
				// No account with the mobile number;
				pw.println("No account with this mobile number");
				response.sendRedirect("MainUserLogin.html");
			}
			pw.println("Came Here 2");
			int pin = Integer.parseInt(request.getParameter("pin"));
			pw.println("Came Here 3");
			if(pin == original_pin)
			{
				rs = DbUtil.st.executeQuery("select cid from customer where mobile = "+mobileNo);
				if(rs.next())
				{
					this.cid = rs.getInt(1);
				}
				response.sendRedirect("UserEnquiryPortal.html");
			}
			else 
			{
				// No account password is wrong;
				response.sendRedirect("MainUserLogin.html");
			}
		}
		catch(Exception e)
		{
			pw.println("Exception Occured");
			pw.println(e.getMessage());
		}
	}
	public void callDeposit(HttpServletRequest request,HttpServletResponse response,int cid,String type)throws ServletException,IOException
	{
		PrintWriter pw = response.getWriter();
		pw.println("Came Here 1");
		pw.println("Static cid = "+this.cid);
		try {
			DbUtil.connect();
			int amount = Integer.parseInt(request.getParameter("amount"));
			int pin = Integer.parseInt(request.getParameter("pin"));
			String query = "select pin from customer where cid="+cid;
			ResultSet rs = DbUtil.st.executeQuery(query);
			pw.println(cid);
			pw.println("Came Here 2");
			if(rs.next())
			{
				if(rs.getInt(1) != pin)
				{
					pw.println("The Pin you entered is Incorrect");
					response.sendRedirect("Deposit.html");
				}
				else 
				{
					// Step 1 : Update in Customer Table and Accounts Table.
					pw.println("Came Here 3");
					String query2 = "select balance from customer where cid="+cid;
					rs = DbUtil.st.executeQuery(query2);
					if(rs.next())
					{
						amount += rs.getInt(1);
						DbUtil.st.execute("update customer set balance="+amount +" where cid="+cid);
						DbUtil.st.execute("update accounts set balance="+amount +" where cid="+cid);
					}
					pw.println("Came Here 4");
					// Step 2 : Update data in transactions table table.
					int TransactionID = 0;
					String query3 = "select COUNT(accNo) from transactions";
					ResultSet r1 = DbUtil.st.executeQuery(query3);
					if(r1.next())
					{
						TransactionID = 1010 + r1.getInt(1);
					} 
					pw.println("Came Here 5");
					String query4 = "select accNo from accounts where cid="+cid;
					ResultSet r2 = DbUtil.st.executeQuery(query4);
					int from_accNo = 0;
					if(r2.next())
					{
						from_accNo = r2.getInt(1);
					}
					int to_AccNo = from_accNo;
					String TransactionType = type;
					int deposit_amount = amount;
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Date date = new Date();
				    String curr_date = (String)(formatter.format(date));
				    
				    String query5 = "insert into transactions values(?,?,?,?,?,?)";
				    PreparedStatement ps2 = DbUtil.conn.prepareStatement(query5);
				    ps2.setInt(1, from_accNo);
				    ps2.setString(2, TransactionType);
				    ps2.setInt(3,deposit_amount);
				    ps2.setString(4, curr_date);
				    ps2.setInt(5,TransactionID);
				    ps2.setInt(6, to_AccNo);
				    ps2.executeUpdate();
				    response.sendRedirect("UserEnquiryPortal.html");
				}
			}
			else 
			{
				pw.println("I did not go");
			}
		}
		catch(Exception e)
		{
			pw.println("Exception Occured");
			pw.println(e.getMessage());
		}
	}
	public void callWithDrawl(HttpServletRequest request,HttpServletResponse response,int cid,String type)throws ServletException,IOException
	{
		PrintWriter pw = response.getWriter();
		pw.println("Came Here 1");
		pw.println("Static cid = "+this.cid);
		try {
			DbUtil.connect();
			int amount = Integer.parseInt(request.getParameter("amount"));
			int pin = Integer.parseInt(request.getParameter("pin"));
			String query = "select pin from customer where cid="+cid;
			ResultSet rs = DbUtil.st.executeQuery(query);
			pw.println(cid);
			pw.println("Came Here 2");
			if(rs.next())
			{
				if(rs.getInt(1) != pin)
				{
					pw.println("The Pin you entered is Incorrect");
					response.sendRedirect("Withdrawl.html");
				}
				else 
				{
					// Step 1 : Update in Customer Table and Accounts Table.
					pw.println("Came Here 3");
					String query2 = "select balance from customer where cid="+cid;
					rs = DbUtil.st.executeQuery(query2);
					if(rs.next())
					{
						if(rs.getInt(1)-amount>0)
						{
							int amount1 = rs.getInt(1)-amount;
							DbUtil.st.execute("update customer set balance="+amount1 +" where cid="+cid);
							DbUtil.st.execute("update accounts set balance="+amount1 +" where cid="+cid);
							
							
							pw.println("Came Here 4");
							// Step 2 : Update data in transactions table table.
							int TransactionID = 0;
							String query3 = "select COUNT(accNo) from transactions";
							ResultSet r1 = DbUtil.st.executeQuery(query3);
							if(r1.next())
							{
								TransactionID = 1010 + r1.getInt(1);
							} 
							pw.println("Came Here 5");
							String query4 = "select accNo from accounts where cid="+cid;
							ResultSet r2 = DbUtil.st.executeQuery(query4);
							int from_accNo = 0;
							if(r2.next())
							{
								from_accNo = r2.getInt(1);
							}
							int to_AccNo = from_accNo;
							String TransactionType = type;
							int deposit_amount = amount;
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							Date date = new Date();
						    String curr_date = (String)(formatter.format(date));
						    
						    String query5 = "insert into transactions values(?,?,?,?,?,?)";
						    PreparedStatement ps2 = DbUtil.conn.prepareStatement(query5);
						    ps2.setInt(1, from_accNo);
						    ps2.setString(2, TransactionType);
						    ps2.setInt(3,deposit_amount);
						    ps2.setString(4, curr_date);
						    ps2.setInt(5,TransactionID);
						    ps2.setInt(6, to_AccNo);
						    ps2.executeUpdate();
						    response.sendRedirect("UserEnquiryPortal.html");
						}
						else 
						{
							pw.println("Insufficient Funds");
							response.sendRedirect("LowFunds.html");
						}
					}
				}
			}
			else 
			{
				pw.println("I did not go");
			}
		}
		catch(Exception e)
		{
			pw.println("Exception Occured");
			pw.println(e.getMessage());
		}
	}
	public void callFundTransfer(HttpServletRequest request,HttpServletResponse response,int cid,String type)throws IOException,ServletException
	{
		PrintWriter pw = response.getWriter();
		try {
			int to_AccNo = Integer.parseInt(request.getParameter("receiveraccNo"));
			int amount = Integer.parseInt(request.getParameter("amount"));
			int pin = Integer.parseInt(request.getParameter("pin"));
			boolean CorrectPin = false;
			boolean to_AccNoExist = false;
			
			// Check fro correct pin or not.
			
			
			String query = "select pin from customer where cid="+cid;
			ResultSet rs = DbUtil.st.executeQuery(query);
			if(rs.next())
			{
				if(rs.getInt(1) != pin)
				{
					pw.println("The Pin you entered is Incorrect");
					response.sendRedirect("FundTransfer.html");
				}
				else 
					CorrectPin = true;
			}
			
			// Check for Correct Account Number or not.
			
			
			rs = DbUtil.st.executeQuery("select * from accounts where accNo="+to_AccNo);
			if(rs.next())
			{
				to_AccNoExist = true;
			}
			else 
			{
				pw.println("The Account Number you are searching for does not exist");
				response.sendRedirect("TransferFailure.html");
			}
			
			
			// Yes Both Receiver Account Number Exist and the Pin is correct.
			if(CorrectPin && to_AccNoExist)
			{	
				
				// Fetching the Receiver Balance and Receiver Customer_Id.
				ResultSet rs2 = DbUtil.st.executeQuery("select cid from accounts where accNo="+to_AccNo);
				int receiver_balance = 0;
				int to_cid = 0;
				if(rs2.next())
				{
					to_cid = rs2.getInt(1);
					rs2 = DbUtil.st.executeQuery("select balance from customer where cid="+to_cid);
					if(rs2.next())
					{
						receiver_balance = rs2.getInt(1);
					}
					else 
						pw.println("Reciever cid found but unable to fetch balance");
				}
				else 
				{
					pw.println("Reciever cid is unable to fetch");
				}
				
				
				// Check Whether Sufficient funds are present with the sender. 
				rs = DbUtil.st.executeQuery("select balance from customer where cid="+cid);
				boolean FundsSufficient = false;
				int sender_balance = 0;
				if(rs.next())
				{
					sender_balance = rs.getInt(1);
					if(sender_balance>=amount)
					{	
						FundsSufficient = true;
					}
					else 
					{
						pw.println("Insufficient Funds");
						response.sendRedirect("LowFunds.html");
					}
				}
				else 
				{
					pw.println("I did not got balance");
				}
				
				
				// Fetech the Sender AccountNUmber.
				int sender_AccNo = 0;
				rs = DbUtil.st.executeQuery("select accNo from accounts where cid="+this.cid);
				if(rs.next())
				{
					sender_AccNo = rs.getInt(1);
				}
				else 
					pw.println("Sender Account Number can't be fetched");
				
				
				
				// If Sender have sufficient funds then 
				if(FundsSufficient)
				{
					// Updating on customer table , accounts table for sender 
					sender_balance -= amount;
					DbUtil.st.execute("update customer set balance="+sender_balance +" where cid="+cid);
					DbUtil.st.execute("update accounts set balance="+sender_balance +" where cid="+cid);
					
					// Updating on customer table, accounts table for recevier 
					receiver_balance += amount;
					DbUtil.st.execute("update customer set balance="+receiver_balance +" where cid="+to_cid);
					DbUtil.st.execute("update accounts set balance="+receiver_balance +" where cid="+to_cid);
					
					
					// Updating on Transactions table from Sender.
					int TransactionID = 0;
					String query3 = "select COUNT(accNo) from transactions";
					ResultSet r1 = DbUtil.st.executeQuery(query3);
					if(r1.next())
					{
						TransactionID = 1010 + r1.getInt(1);
					}
					String query5 = "insert into transactions values(?,?,?,?,?,?)";
				    PreparedStatement ps2 = DbUtil.conn.prepareStatement(query5);
					ps2.setInt(1,sender_AccNo);
					ps2.setString(2, type);
					ps2.setInt(3,amount);
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Date date = new Date();
				    String curr_date = (String)(formatter.format(date));
				    ps2.setString(4, curr_date);
				    ps2.setInt(5,TransactionID);
				    ps2.setInt(6,to_AccNo);
				    ps2.executeUpdate();
				    response.sendRedirect("UserEnquiryPortal.html");
				}
				else 
					response.sendRedirect("LowFunds.html");
			}
		}
		catch(Exception e)
		{
			pw.println("Exception Occured");
			pw.println(e.getMessage());
		}
	}
}
