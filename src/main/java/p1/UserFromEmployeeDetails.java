package p1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import p2.DbUtil;

@WebServlet("/UserFromEmployeeDetails")
public class UserFromEmployeeDetails extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public UserFromEmployeeDetails() {
        super();
    }

    @Override
    protected void service(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter pw = response.getWriter();

        try {
            long phno = Long.parseLong(request.getParameter("Phno"));

            Connection conn = DbUtil.getConnection();

            String query =
                    "SELECT * FROM customer WHERE mobile = ?";

            try (PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setLong(1, phno);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        pw.println("<html><body align='center' bgcolor='yellow'>");
                        pw.println("<table border='1' align='center'>");

                        pw.println(
                            "<tr>" +
                            "<th>Id</th>" +
                            "<th>Name</th>" +
                            "<th>Mobile</th>" +
                            "<th>Email</th>" +
                            "<th>Aadhaar</th>" +
                            "<th>Pan</th>" +
                            "<th>Pin</th>" +
                            "<th>Balance</th>" +
                            "</tr>"
                        );

                        pw.println("<tr>");
                        pw.println("<td>" + rs.getInt(1) + "</td>");
                        pw.println("<td>" + rs.getString(2) + "</td>");
                        pw.println("<td>" + rs.getLong(3) + "</td>");
                        pw.println("<td>" + rs.getString(4) + "</td>");
                        pw.println("<td>" + rs.getLong(5) + "</td>");
                        pw.println("<td>" + rs.getString(6) + "</td>");
                        pw.println("<td>****</td>");
                        pw.println("<td>" + rs.getString(8) + "</td>");
                        pw.println("</tr>");

                        pw.println("</table>");
                        pw.println("</body></html>");

                    } else {
                        response.sendRedirect("SorryNotFound.html");
                    }
                }
            }

        } catch (NumberFormatException e) {

            response.sendRedirect("SorryNotFound.html");

        } catch (Exception e) {

            throw new ServletException(
                    "Unable to retrieve customer details.",
                    e
            );
        }
    }
}