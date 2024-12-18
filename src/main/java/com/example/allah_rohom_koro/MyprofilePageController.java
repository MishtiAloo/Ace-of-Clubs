package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyprofilePageController {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private Text tv_MPP_info;

    @FXML
    private Button btn_LogOut;

    @FXML
    private Button btn_DeletAcc;

    public static String globalUser;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/A2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "an32bell206";

    // Method to initialize user profile data when the page is loaded
    public void initialize() {
        if (globalUser != null && !globalUser.isEmpty()) {
            loadUserData(globalUser);
        } else {
            tv_MPP_info.setText("No user is logged in.");
        }
    }

    @FXML
    void logOut (MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void deleteAcc(MouseEvent event) {
        if (globalUser != null && !globalUser.isEmpty()) {
            // Connect to the database
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // SQL query to delete the user from the database
                String query = "DELETE FROM P_Users WHERE username = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, globalUser);

                int rowsAffected = statement.executeUpdate(); // Execute the deletion

                if (rowsAffected > 0) {
                    System.out.println("User deleted successfully.");

                    // Clear the global user and redirect to the login page
                    globalUser = null;
                    root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    System.out.println("User not found or already deleted.");
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                tv_MPP_info.setText("Error deleting user account.");
            }
        } else {
            tv_MPP_info.setText("No user is logged in.");
        }
    }


    @FXML
    void goBack(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Method to load user data from the database
    private void loadUserData(String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT username, email, studentID, batch, year, dept, interest, ownclub, position FROM P_Users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Fetch user data
                String email = resultSet.getString("email");
                String studentID = resultSet.getString("studentID");
                String batch = resultSet.getString("batch");
                String year = resultSet.getString("year");
                String dept = resultSet.getString("dept");
                String interest = resultSet.getString("interest");
                String ownClub = resultSet.getString("ownclub");
                String position = resultSet.getString("position");

                // Set the user data to the tv_MPP_info Text field
                String userInfo = String.format("Username: %s\nEmail: %s\nStudent ID: %s\nBatch: %s\nYear: %s\nDept: %s\nInterest: %s\nOwn Club: %s\nPosition: %s",
                        username, email, studentID, batch, year, dept, interest, ownClub, position);

                tv_MPP_info.setText(userInfo);
            } else {
                tv_MPP_info.setText("User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            tv_MPP_info.setText("Error fetching user data.");
        }
    }
}
