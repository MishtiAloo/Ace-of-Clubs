package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class RecruitmentItemController {

    public static String globaluser;

    @FXML
    private Text clubNameText;

    @FXML
    private Text positionText;

    @FXML
    private Text slotsText;

    @FXML
    private Text endingDateText;

    @FXML
    private Button btn_RI_apply;

    private String clubName; // Store the club name for this item

    // Method to set recruitment data in the RecruitmentItem
    public void setRecruitmentData(String clubName, String position, String endingDate, int slots, boolean isApplied, boolean isAdmin) {
        this.clubName = clubName;
        clubNameText.setText(clubName);
        positionText.setText(position);
        slotsText.setText("Empty Positions: " + slots);
        endingDateText.setText("Ending Date: " + endingDate);

        if (isAdmin) {
            // For admins, hide the apply button and display "Admin cannot apply"
            btn_RI_apply.setVisible(false);
            Text adminText = new Text("Admin cannot apply");
            adminText.setStyle("-fx-fill: white; -fx-font-size: 16px;");
            ((AnchorPane) btn_RI_apply.getParent()).getChildren().add(adminText);
            adminText.setLayoutX(btn_RI_apply.getLayoutX());
            adminText.setLayoutY(btn_RI_apply.getLayoutY() + 50);
        } else if (isApplied) {
            // If the user is already applied, hide the apply button and show "Already Applied"
            btn_RI_apply.setVisible(false);
            Text alreadyAppliedText = new Text("Already Applied");
            alreadyAppliedText.setStyle("-fx-fill: white; -fx-font-size: 16px;");
            ((AnchorPane) btn_RI_apply.getParent()).getChildren().add(alreadyAppliedText);
            alreadyAppliedText.setLayoutX(btn_RI_apply.getLayoutX());
            alreadyAppliedText.setLayoutY(btn_RI_apply.getLayoutY() + 50);
        }
    }





    // Method to handle the Apply button click
    @FXML
    public void applyForRecruitment() {
        String globalUser = RecruitPageController.globaluser;  // Get global user from RecruitPageController
        String url = "jdbc:mysql://localhost:3306/A2";  // Replace with your DB details
        String user = "root";  // Replace with your DB username
        String password = "an32bell206";  // Replace with your DB password

        // First, fetch the usertype of the current user
        String userTypeQuery = "SELECT usertype FROM p_users WHERE username = ?";
        String updateWaitingQuery = "UPDATE p_recruitments SET Waiting = ? WHERE `Club name` = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement userTypeStmt = conn.prepareStatement(userTypeQuery)) {

            // Check user type
            userTypeStmt.setString(1, globalUser);
            ResultSet rs = userTypeStmt.executeQuery();

            if (rs.next()) {
                String userType = rs.getString("usertype");

                // Only allow non-admin users to apply
                if (!"admin".equalsIgnoreCase(userType)) {
                    // Now fetch the current Waiting list for this club
                    String currentWaiting = fetchCurrentWaitingList(conn, clubName);

                    // Add the current user to the waiting list
                    String updatedWaiting = updateWaitingList(currentWaiting, globalUser);

                    // Now update the Waiting column in the database
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateWaitingQuery)) {
                        updateStmt.setString(1, updatedWaiting);
                        updateStmt.setString(2, clubName);
                        updateStmt.executeUpdate();

                        // After successful registration, hide the button and show "Already registered"
                        btn_RI_apply.setVisible(false);
                        Text alreadyRegisteredText = new Text("Already registered");
                        alreadyRegisteredText.setStyle("-fx-fill: white; -fx-font-size: 16px;");
                        ((AnchorPane) btn_RI_apply.getParent()).getChildren().add(alreadyRegisteredText);
                        alreadyRegisteredText.setLayoutX(btn_RI_apply.getLayoutX());
                        alreadyRegisteredText.setLayoutY(btn_RI_apply.getLayoutY() + 50);
                    }
                } else {

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch the current Waiting list
    private String fetchCurrentWaitingList(Connection conn, String clubName) throws SQLException {
        String query = "SELECT Waiting FROM p_recruitments WHERE `Club name` = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, clubName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Waiting");
            }
        }
        return "";
    }

    // Method to update the Waiting list by appending the globaluser
    private String updateWaitingList(String currentWaiting, String globalUser) {
        if (currentWaiting == null || currentWaiting.isEmpty()) {
            return globalUser;
        } else {
            return currentWaiting + "," + globalUser;
        }
    }
}
