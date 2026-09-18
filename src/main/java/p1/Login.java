package p1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import p2.DbUtil;

@WebServlet("/Login")
public class Login extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("ID"));
            String password = request.getParameter("pwd");

            Connection conn = DbUtil.getConnection();

            String sql =
                "SELECT empid FROM employee WHERE empid = ? AND password = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        response.sendRedirect("StaffEnquiryPortal.html");
                    } else {
                        response.sendRedirect("WrongCredentials.html");
                    }
                }
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("WrongCredentials.html");

        } catch (Exception e) {
            throw new ServletException("Login processing failed.", e);
        }
    }
}