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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

/**
 *
 * @author Paledi.Egnitius
 */
@WebServlet("/FlightDataRetriever")
public class FlightDataRetriever extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/SkyTravel";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "12345";

    private static class Flight {

        private int flightsId;
        private String departure;
        private String destination;
        private String time;
        private String status;
        private int baggage;
        private int passengers;

        public Flight(int flightsId, String departure, String destination, String time, String status, int baggage, int passengers) {
            this.flightsId = flightsId;
            this.departure = departure;
            this.destination = destination;
            this.time = time;
            this.status = status;
            this.baggage = baggage;
            this.passengers = passengers;
        }

        // Add getters and setters if needed
        public int getFlightId() {
            return flightsId;
        }

        public void setFlightId(int flightId) {
            this.flightsId = flightId;
        }

        public String getDeparture() {
            return departure;
        }

        public void setDeparture(String departure) {
            this.departure = departure;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getBaggage() {
            return baggage;
        }

        public void setBaggage(int baggage) {
            this.baggage = baggage;
        }

        public int getPassengers() {
            return passengers;
        }

        public void setPassengers(int passengers) {
            this.passengers = passengers;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Flight> flights = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish a database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // Prepare the SQL statement to retrieve flight data
            String sql = "SELECT * FROM flights";
            statement = connection.prepareStatement(sql);

            // Execute the SQL statement and retrieve the result set
            resultSet = statement.executeQuery();

            // Iterate over the result set and create Flight objects
            while (resultSet.next()) {
                int flightsId = resultSet.getInt("flightsID");
                String departure = resultSet.getString("Departure");
                String destination = resultSet.getString("Destination");
                String time = resultSet.getString("Time");
                String status = resultSet.getString("Status");
                int baggage = resultSet.getInt("Baggage");
                int passengers = resultSet.getInt("Passengers");

                Flight flight = new Flight(flightsId, departure, destination, time, status, baggage, passengers);
                flights.add(flight);
            }

            // Set the response content type to JSON
            response.setContentType("application/json");

            // Convert the flights list to JSON and send it as the response
            response.getWriter().write(new Gson().toJson(flights));
        } catch (ClassNotFoundException | SQLException e) {
            // Handle any errors that occurred during database operations
            e.printStackTrace();
            // Set the response status to indicate a server error
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            // Close the result set, statement, and connection
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
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
