package p2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtil {

    public static Connection conn;
    public static Statement st;

    public static void connect() {

        String dbUrl = System.getenv("BANK_DB_URL");
        String dbUser = System.getenv("BANK_DB_USER");
        String dbPassword = System.getenv("BANK_DB_PASSWORD");

        if (dbUrl == null || dbUser == null || dbPassword == null) {
            throw new IllegalStateException(
                "Database environment variables are not configured."
            );
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(
                dbUrl,
                dbUser,
                dbPassword
            );

            st = conn.createStatement();

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                "MySQL JDBC driver was not found.",
                e
            );

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Unable to connect to the database.",
                e
            );
        }
    }

    public static Connection getConnection() {

        connect();
        return conn;
    }
}