package p1;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import p2.DbUtil;

@WebServlet("/UserAccountLogin")
public class UserAccountLogin extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /*
     * Keeps the existing application design.
     * Note: a static cid is shared between servlet requests/users.
     * Session-based authentication would be a better future improvement.
     */
    static int cid = 0;

    public UserAccountLogin() {
        super();
    }

    @Override
    protected void service(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String choice = request.getParameter("type");

        if (choice == null) {
            response.getWriter().write("Invalid request.");
            return;
        }

        switch (choice) {

            case "Login":
                callLogin(request, response);
                break;

            case "Deposit":
                callDeposit(request, response, this.cid, choice);
                break;

            case "WithDrawl":
                callWithDrawl(request, response, this.cid, choice);
                break;

            case "FundTransfer":
                callFundTransfer(request, response, this.cid, choice);
                break;

            case "GetBalance":
                getBalance(request, response, this.cid);
                break;

            case "ShowUserDetails":
                showDetails(request, response);
                break;

            case "OK":
                callTransactions(request, response, this.cid);
                break;

            default:
                response.getWriter().write("Invalid operation.");
                break;
        }
    }

    public void callTransactions(
            HttpServletRequest request,
            HttpServletResponse response,
            int cid2)
            throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        String accountQuery =
                "SELECT accNo FROM accounts WHERE cid = ?";

        String transactionQuery =
                "SELECT * FROM transactions WHERE accNo = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement accountPs =
                     conn.prepareStatement(accountQuery);
             PreparedStatement transactionPs =
                     conn.prepareStatement(transactionQuery)) {

            accountPs.setInt(1, cid2);

            int accNo = 0;

            try (ResultSet rs = accountPs.executeQuery()) {
                if (rs.next()) {
                    accNo = rs.getInt(1);
                } else {
                    pw.println("The data is not found");
                    return;
                }
            }

            transactionPs.setInt(1, accNo);

            try (ResultSet rs = transactionPs.executeQuery()) {

                pw.println("<html><body align='center' bgcolor='yellow'>");
                pw.println("<table border='1' align='center'>");

                pw.println(
                    "<tr>" +
                    "<th>Account Number</th>" +
                    "<th>Transaction Type</th>" +
                    "<th>Amount</th>" +
                    "<th>Time</th>" +
                    "<th>Transaction ID</th>" +
                    "<th>Recipient Account Number</th>" +
                    "</tr>"
                );

                while (rs.next()) {

                    pw.println("<tr>");
                    pw.println("<td>" + rs.getInt(1) + "</td>");
                    pw.println("<td>" + rs.getString(2) + "</td>");
                    pw.println("<td>" + rs.getInt(3) + "</td>");
                    pw.println("<td>" + rs.getString(4) + "</td>");
                    pw.println("<td>" + rs.getInt(5) + "</td>");
                    pw.println("<td>" + rs.getInt(6) + "</td>");
                    pw.println("</tr>");
                }

                pw.println("</table>");
                pw.println("</body></html>");
            }

        } catch (Exception e) {
            pw.println("Exception occurred");
            pw.println(e.getMessage());
        }
    }

    private void getBalance(
            HttpServletRequest request,
            HttpServletResponse response,
            int cid2) {

        String query =
                "SELECT balance FROM customer WHERE cid = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, cid2);

            int balance = 0;

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    balance = rs.getInt(1);
                } else {
                    response.getWriter().write("The data is not found");
                    return;
                }
            }

            request.setAttribute("balance", balance);

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("CurrentBalance.jsp");

            dispatcher.forward(request, response);

        } catch (Exception e) {
            try {
                response.getWriter().println("Exception occurred");
                response.getWriter().println(e.getMessage());
            } catch (IOException ignored) {
                // Ignore secondary response error
            }
        }
    }

    public void showDetails(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter pw = response.getWriter();

        String customerQuery =
                "SELECT * FROM customer WHERE cid = ?";

        String accountQuery =
                "SELECT accNo FROM accounts WHERE cid = ?";

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement customerPs =
                     conn.prepareStatement(customerQuery);
             PreparedStatement accountPs =
                     conn.prepareStatement(accountQuery)) {

            customerPs.setInt(1, this.cid);

            int balance = 0;
            int custoId = 0;
            int pin = 0;
            long mobile = 0;
            long aadhaar = 0;
            String pan = "";
            String name = "";
            String email = "";
            int accNo = 0;

            try (ResultSet rs = customerPs.executeQuery()) {

                if (rs.next()) {

                    custoId = rs.getInt(1);
                    name = rs.getString(2);
                    mobile = rs.getLong(3);
                    email = rs.getString(4);
                    aadhaar = rs.getLong(5);
                    pan = rs.getString(6);
                    pin = rs.getInt(7);
                    balance = rs.getInt(8);

                } else {
                    pw.println("Customer details not found.");
                    return;
                }
            }

            accountPs.setInt(1, this.cid);

            try (ResultSet rs = accountPs.executeQuery()) {
                if (rs.next()) {
                    accNo = rs.getInt(1);
                }
            }

            request.setAttribute("cid", custoId);
            request.setAttribute("name", name);
            request.setAttribute("mobile", mobile);
            request.setAttribute("email", email);
            request.setAttribute("aadhaar", aadhaar);
            request.setAttribute("pan", pan);
            request.setAttribute("pin", pin);
            request.setAttribute("balance", balance);
            request.setAttribute("accNo", accNo);

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("UserDetails.jsp");

            dispatcher.forward(request, response);

        } catch (Exception e) {
            pw.println("Exception occurred");
            pw.println(e.getMessage());
        }
    }

    public void callLogin(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter pw = response.getWriter();

        String mobileParam = request.getParameter("mobile");
        String pinParam = request.getParameter("pin");

        try {

            long mobileNo = Long.parseLong(mobileParam);
            int pin = Integer.parseInt(pinParam);

            String pinQuery =
                    "SELECT pin FROM customer WHERE mobile = ?";

            String customerQuery =
                    "SELECT cid FROM customer WHERE mobile = ?";

            try (Connection conn = DbUtil.getConnection();
                 PreparedStatement pinPs =
                         conn.prepareStatement(pinQuery);
                 PreparedStatement customerPs =
                         conn.prepareStatement(customerQuery)) {

                pinPs.setLong(1, mobileNo);

                int originalPin;

                try (ResultSet rs = pinPs.executeQuery()) {

                    if (rs.next()) {
                        originalPin = rs.getInt(1);
                    } else {
                        response.sendRedirect("MainUserLogin.html");
                        return;
                    }
                }

                if (pin != originalPin) {
                    response.sendRedirect("MainUserLogin.html");
                    return;
                }

                customerPs.setLong(1, mobileNo);

                try (ResultSet rs = customerPs.executeQuery()) {

                    if (rs.next()) {
                        this.cid = rs.getInt(1);
                    } else {
                        response.sendRedirect("MainUserLogin.html");
                        return;
                    }
                }

                response.sendRedirect("UserEnquiryPortal.html");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("MainUserLogin.html");

        } catch (Exception e) {
            pw.println("Exception occurred");
            pw.println(e.getMessage());
        }
    }

    public void callDeposit(
            HttpServletRequest request,
            HttpServletResponse response,
            int cid,
            String type)
            throws ServletException, IOException {

        PrintWriter pw = response.getWriter();

        try {

            int amount = Integer.parseInt(
                    request.getParameter("amount"));

            int pin = Integer.parseInt(
                    request.getParameter("pin"));

            String pinQuery =
                    "SELECT pin FROM customer WHERE cid = ?";

            String balanceQuery =
                    "SELECT balance FROM customer WHERE cid = ?";

            String accountQuery =
                    "SELECT accNo FROM accounts WHERE cid = ?";

            String transactionCountQuery =
                    "SELECT COUNT(accNo) FROM transactions";

            String transactionInsert =
                    "INSERT INTO transactions " +
                    "(accNo, transactionType, amount, transactionDate, " +
                    "transactionId, recipientAccNo) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            String customerUpdate =
                    "UPDATE customer SET balance = ? WHERE cid = ?";

            String accountUpdate =
                    "UPDATE accounts SET balance = ? WHERE cid = ?";

            try (Connection conn = DbUtil.getConnection();
                 PreparedStatement pinPs =
                         conn.prepareStatement(pinQuery);
                 PreparedStatement balancePs =
                         conn.prepareStatement(balanceQuery);
                 PreparedStatement customerUpdatePs =
                         conn.prepareStatement(customerUpdate);
                 PreparedStatement accountUpdatePs =
                         conn.prepareStatement(accountUpdate);
                 PreparedStatement transactionCountPs =
                         conn.prepareStatement(transactionCountQuery);
                 PreparedStatement accountPs =
                         conn.prepareStatement(accountQuery);
                 PreparedStatement transactionPs =
                         conn.prepareStatement(transactionInsert)) {

                pinPs.setInt(1, cid);

                try (ResultSet rs = pinPs.executeQuery()) {

                    if (!rs.next()) {
                        response.sendRedirect("Deposit.html");
                        return;
                    }

                    if (rs.getInt(1) != pin) {
                        response.sendRedirect("Deposit.html");
                        return;
                    }
                }

                balancePs.setInt(1, cid);

                int currentBalance;

                try (ResultSet rs = balancePs.executeQuery()) {

                    if (!rs.next()) {
                        pw.println("Unable to retrieve balance.");
                        return;
                    }

                    currentBalance = rs.getInt(1);
                }

                int newBalance = currentBalance + amount;

                customerUpdatePs.setInt(1, newBalance);
                customerUpdatePs.setInt(2, cid);
                customerUpdatePs.executeUpdate();

                accountUpdatePs.setInt(1, newBalance);
                accountUpdatePs.setInt(2, cid);
                accountUpdatePs.executeUpdate();

                int transactionId = 1010;

                try (ResultSet rs =
                             transactionCountPs.executeQuery()) {

                    if (rs.next()) {
                        transactionId =
                                1010 + rs.getInt(1);
                    }
                }

                int fromAccNo = 0;

                accountPs.setInt(1, cid);

                try (ResultSet rs = accountPs.executeQuery()) {

                    if (rs.next()) {
                        fromAccNo = rs.getInt(1);
                    } else {
                        pw.println("Account number not found.");
                        return;
                    }
                }

                int toAccNo = fromAccNo;

                String transactionDate =
                        new SimpleDateFormat(
                                "dd/MM/yyyy HH:mm:ss")
                                .format(new Date());

                transactionPs.setInt(1, fromAccNo);
                transactionPs.setString(2, type);

                // Record the amount of this transaction,
                // not the resulting account balance.
                transactionPs.setInt(3, amount);

                transactionPs.setString(4, transactionDate);
                transactionPs.setInt(5, transactionId);
                transactionPs.setInt(6, toAccNo);

                transactionPs.executeUpdate();

                response.sendRedirect(
                        "UserEnquiryPortal.html");
            }

        } catch (NumberFormatException e) {

            response.sendRedirect("Deposit.html");

        } catch (Exception e) {

            pw.println("Exception occurred");
            pw.println(e.getMessage());
        }
    }

    public void callWithDrawl(
            HttpServletRequest request,
            HttpServletResponse response,
            int cid,
            String type)
            throws ServletException, IOException {

        PrintWriter pw = response.getWriter();

        try {

            int amount = Integer.parseInt(
                    request.getParameter("amount"));

            int pin = Integer.parseInt(
                    request.getParameter("pin"));

            String pinQuery =
                    "SELECT pin FROM customer WHERE cid = ?";

            String balanceQuery =
                    "SELECT balance FROM customer WHERE cid = ?";

            String accountQuery =
                    "SELECT accNo FROM accounts WHERE cid = ?";

            String transactionCountQuery =
                    "SELECT COUNT(accNo) FROM transactions";

            String transactionInsert =
                    "INSERT INTO transactions " +
                    "(accNo, transactionType, amount, transactionDate, " +
                    "transactionId, recipientAccNo) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            String customerUpdate =
                    "UPDATE customer SET balance = ? WHERE cid = ?";

            String accountUpdate =
                    "UPDATE accounts SET balance = ? WHERE cid = ?";

            try (Connection conn = DbUtil.getConnection();
                 PreparedStatement pinPs =
                         conn.prepareStatement(pinQuery);
                 PreparedStatement balancePs =
                         conn.prepareStatement(balanceQuery);
                 PreparedStatement customerUpdatePs =
                         conn.prepareStatement(customerUpdate);
                 PreparedStatement accountUpdatePs =
                         conn.prepareStatement(accountUpdate);
                 PreparedStatement transactionCountPs =
                         conn.prepareStatement(transactionCountQuery);
                 PreparedStatement accountPs =
                         conn.prepareStatement(accountQuery);
                 PreparedStatement transactionPs =
                         conn.prepareStatement(transactionInsert)) {

                pinPs.setInt(1, cid);

                try (ResultSet rs = pinPs.executeQuery()) {

                    if (!rs.next()) {
                        response.sendRedirect("Withdrawl.html");
                        return;
                    }

                    if (rs.getInt(1) != pin) {
                        response.sendRedirect("Withdrawl.html");
                        return;
                    }
                }

                balancePs.setInt(1, cid);

                int currentBalance;

                try (ResultSet rs = balancePs.executeQuery()) {

                    if (!rs.next()) {
                        pw.println("Unable to retrieve balance.");
                        return;
                    }

                    currentBalance = rs.getInt(1);
                }

                if (currentBalance - amount < 0) {
                    response.sendRedirect("LowFunds.html");
                    return;
                }

                int newBalance = currentBalance - amount;

                customerUpdatePs.setInt(1, newBalance);
                customerUpdatePs.setInt(2, cid);
                customerUpdatePs.executeUpdate();

                accountUpdatePs.setInt(1, newBalance);
                accountUpdatePs.setInt(2, cid);
                accountUpdatePs.executeUpdate();

                int transactionId = 1010;

                try (ResultSet rs =
                             transactionCountPs.executeQuery()) {

                    if (rs.next()) {
                        transactionId =
                                1010 + rs.getInt(1);
                    }
                }

                int fromAccNo = 0;

                accountPs.setInt(1, cid);

                try (ResultSet rs = accountPs.executeQuery()) {

                    if (rs.next()) {
                        fromAccNo = rs.getInt(1);
                    } else {
                        pw.println("Account number not found.");
                        return;
                    }
                }

                int toAccNo = fromAccNo;

                String transactionDate =
                        new SimpleDateFormat(
                                "dd/MM/yyyy HH:mm:ss")
                                .format(new Date());

                transactionPs.setInt(1, fromAccNo);
                transactionPs.setString(2, type);
                transactionPs.setInt(3, amount);
                transactionPs.setString(4, transactionDate);
                transactionPs.setInt(5, transactionId);
                transactionPs.setInt(6, toAccNo);

                transactionPs.executeUpdate();

                response.sendRedirect(
                        "UserEnquiryPortal.html");
            }

        } catch (NumberFormatException e) {

            response.sendRedirect("Withdrawl.html");

        } catch (Exception e) {

            pw.println("Exception occurred");
            pw.println(e.getMessage());
        }
    }

    public void callFundTransfer(
            HttpServletRequest request,
            HttpServletResponse response,
            int cid,
            String type)
            throws IOException, ServletException {

        PrintWriter pw = response.getWriter();

        try {

            int toAccNo = Integer.parseInt(
                    request.getParameter("receiveraccNo"));

            int amount = Integer.parseInt(
                    request.getParameter("amount"));

            int pin = Integer.parseInt(
                    request.getParameter("pin"));

            boolean correctPin = false;
            boolean receiverExists = false;

            String pinQuery =
                    "SELECT pin FROM customer WHERE cid = ?";

            String receiverAccountQuery =
                    "SELECT cid FROM accounts WHERE accNo = ?";

            try (Connection conn = DbUtil.getConnection();
                 PreparedStatement pinPs =
                         conn.prepareStatement(pinQuery);
                 PreparedStatement receiverPs =
                         conn.prepareStatement(receiverAccountQuery)) {

                /*
                 * Validate sender PIN.
                 */
                pinPs.setInt(1, cid);

                try (ResultSet rs = pinPs.executeQuery()) {

                    if (rs.next()) {

                        if (rs.getInt(1) == pin) {
                            correctPin = true;
                        }

                    } else {

                        response.sendRedirect(
                                "FundTransfer.html");
                        return;
                    }
                }

                if (!correctPin) {
                    response.sendRedirect("FundTransfer.html");
                    return;
                }

                /*
                 * Validate receiver account.
                 */
                receiverPs.setInt(1, toAccNo);

                int toCid;

                try (ResultSet rs =
                             receiverPs.executeQuery()) {

                    if (!rs.next()) {
                        response.sendRedirect(
                                "TransferFailure.html");
                        return;
                    }

                    receiverExists = true;
                    toCid = rs.getInt(1);
                }

                if (!receiverExists) {
                    response.sendRedirect(
                            "TransferFailure.html");
                    return;
                }

                String receiverBalanceQuery =
                        "SELECT balance FROM customer WHERE cid = ?";

                String senderBalanceQuery =
                        "SELECT balance FROM customer WHERE cid = ?";

                String senderAccountQuery =
                        "SELECT accNo FROM accounts WHERE cid = ?";

                try (PreparedStatement receiverBalancePs =
                             conn.prepareStatement(receiverBalanceQuery);
                     PreparedStatement senderBalancePs =
                             conn.prepareStatement(senderBalanceQuery);
                     PreparedStatement senderAccountPs =
                             conn.prepareStatement(senderAccountQuery)) {

                    int receiverBalance = 0;
                    int senderBalance = 0;
                    int senderAccNo = 0;

                    /*
                     * Receiver balance.
                     */
                    receiverBalancePs.setInt(1, toCid);

                    try (ResultSet rs =
                                 receiverBalancePs.executeQuery()) {

                        if (rs.next()) {
                            receiverBalance = rs.getInt(1);
                        } else {
                            pw.println(
                                    "Unable to fetch receiver balance."
                            );
                            return;
                        }
                    }

                    /*
                     * Sender balance.
                     */
                    senderBalancePs.setInt(1, cid);

                    try (ResultSet rs =
                                 senderBalancePs.executeQuery()) {

                        if (rs.next()) {
                            senderBalance = rs.getInt(1);
                        } else {
                            pw.println(
                                    "Unable to fetch sender balance."
                            );
                            return;
                        }
                    }

                    if (senderBalance < amount) {
                        response.sendRedirect("LowFunds.html");
                        return;
                    }

                    /*
                     * Sender account number.
                     */
                    senderAccountPs.setInt(1, cid);

                    try (ResultSet rs =
                                 senderAccountPs.executeQuery()) {

                        if (rs.next()) {
                            senderAccNo = rs.getInt(1);
                        } else {
                            pw.println(
                                    "Sender Account Number can't be fetched"
                            );
                            return;
                        }
                    }

                    /*
                     * Update sender and receiver balances.
                     */
                    int updatedSenderBalance =
                            senderBalance - amount;

                    int updatedReceiverBalance =
                            receiverBalance + amount;

                    String updateCustomer =
                            "UPDATE customer SET balance = ? " +
                            "WHERE cid = ?";

                    String updateAccount =
                            "UPDATE accounts SET balance = ? " +
                            "WHERE cid = ?";

                    try (PreparedStatement senderCustomerPs =
                                 conn.prepareStatement(updateCustomer);
                         PreparedStatement receiverCustomerPs =
                                 conn.prepareStatement(updateCustomer);
                         PreparedStatement senderAccountPs2 =
                                 conn.prepareStatement(updateAccount);
                         PreparedStatement receiverAccountPs =
                                 conn.prepareStatement(updateAccount)) {

                        senderCustomerPs.setInt(
                                1,
                                updatedSenderBalance
                        );
                        senderCustomerPs.setInt(2, cid);
                        senderCustomerPs.executeUpdate();

                        receiverCustomerPs.setInt(
                                1,
                                updatedReceiverBalance
                        );
                        receiverCustomerPs.setInt(2, toCid);
                        receiverCustomerPs.executeUpdate();

                        senderAccountPs2.setInt(
                                1,
                                updatedSenderBalance
                        );
                        senderAccountPs2.setInt(2, cid);
                        senderAccountPs2.executeUpdate();

                        receiverAccountPs.setInt(
                                1,
                                updatedReceiverBalance
                        );
                        receiverAccountPs.setInt(2, toCid);
                        receiverAccountPs.executeUpdate();
                    }

                    /*
                     * Create transaction record.
                     */
                    String transactionCountQuery =
                            "SELECT COUNT(accNo) " +
                            "FROM transactions";

                    int transactionId = 1010;

                    try (PreparedStatement countPs =
                                 conn.prepareStatement(
                                         transactionCountQuery);
                         ResultSet rs =
                                 countPs.executeQuery()) {

                        if (rs.next()) {
                            transactionId =
                                    1010 + rs.getInt(1);
                        }
                    }

                    String transactionInsert =
                            "INSERT INTO transactions " +
                            "(accNo, transactionType, amount, " +
                            "transactionDate, transactionId, " +
                            "recipientAccNo) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement transactionPs =
                                 conn.prepareStatement(
                                         transactionInsert)) {

                        String transactionDate =
                                new SimpleDateFormat(
                                        "dd/MM/yyyy HH:mm:ss")
                                        .format(new Date());

                        transactionPs.setInt(
                                1,
                                senderAccNo
                        );

                        transactionPs.setString(
                                2,
                                type
                        );

                        transactionPs.setInt(
                                3,
                                amount
                        );

                        transactionPs.setString(
                                4,
                                transactionDate
                        );

                        transactionPs.setInt(
                                5,
                                transactionId
                        );

                        transactionPs.setInt(
                                6,
                                toAccNo
                        );

                        transactionPs.executeUpdate();
                    }

                    response.sendRedirect(
                            "UserEnquiryPortal.html"
                    );
                }
            }

        } catch (NumberFormatException e) {

            response.sendRedirect("FundTransfer.html");

        } catch (Exception e) {

            pw.println("Exception occurred");
            pw.println(e.getMessage());
        }
    }
}