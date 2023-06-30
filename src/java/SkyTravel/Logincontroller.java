/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SkyTravel;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Paledi.Egnitius
 */
@WebServlet("/login")
public class Logincontroller extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data from the request
        String fullName = request.getParameter("fullname");
        String email = request.getParameter("email");
        String contactNo = request.getParameter("contactno");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");

        // Validate the data
        if (fullName.isEmpty() || email.isEmpty() || contactNo.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // Handle empty fields error
            String errorMessage = "NO feilds should be empty";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("login.html").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            // Handle password mismatch error
            String errorMessage = "Passwords do not match.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("login.html").forward(request, response);
            return;
        }
        // Store the data in the database
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            // Establish a database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/?user=root";
            connection = DriverManager.getConnection(url);

            // Prepare the SQL statement to insert the data
            String sql = "INSERT INTO users (fullname, email, contactno, password) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, fullName);
            statement.setString(2, email);
            statement.setString(3, contactNo);
            statement.setString(4, password);

            // Execute the SQL statement
            statement.executeUpdate();

            // Redirect to a success page or display a success message
            response.sendRedirect("profile.html");
        } catch (ClassNotFoundException | SQLException e) {
            // Handle any errors that occurred during database operations
            e.printStackTrace();
            // Redirect to an error page or display an error message
            String errorMessage = "Failed to save data.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("profile.html").forward(request, response);
        } finally {
            // Close the statement and connection
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
