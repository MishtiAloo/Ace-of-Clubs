package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WaitingRecruitItemController {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/A2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "an32bell206";

    @FXML
    public Text clubNameText;

    @FXML
    public Text positionText;

    @FXML
    public Text applicantName;

    @FXML
    public Text endingDateText;

    public void setApplicantName(String applicant) {
        applicantName.setText(applicant);
    }

    public void setClubName(String clubName) {
        clubNameText.setText(clubName);
    }

    public void setPosition(String position) {
        positionText.setText(position);
    }

    public void setEndingDate(String endDate) {
        endingDateText.setText("Ending Date: " + endDate);
    }

    // Accept applicant: Move from waiting list to accepted in recruitment
    @FXML
    void acceptApplicant(MouseEvent event) {
        String clubName = clubNameText.getText();
        String position = positionText.getText();
        String applicant = applicantName.getText();

        System.out.println("Attempting to accept applicant: " + applicant + " for position: " + position + " in club: " + clubName);

        // Update the applicant status in recruitment (remove them from the waiting list)
        updateApplicantStatus(clubName, position, applicant, "ACCEPTED");
    }

    // Decline applicant: Simply remove from waiting list
    @FXML
    void declineApplicant(MouseEvent event) {
        String clubName = clubNameText.getText();
        String position = positionText.getText();
        String applicant = applicantName.getText();

        System.out.println("Attempting to decline applicant: " + applicant + " for position: " + position + " in club: " + clubName);

        // Update the applicant status in recruitment (remove them from the waiting list)
        updateApplicantStatus(clubName, position, applicant, "DECLINED");
    }

    // Update applicant status and remove them from the waiting list
    private void updateApplicantStatus(String clubName, String position, String applicant, String action) {
        // SQL query to update the waiting list status
        String sqlUpdate = "UPDATE p_recruitments SET `Waiting` = NULL WHERE `Club name` = ? AND Position = ? AND `Waiting` = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {

            // Set values in the SQL query
            pstmt.setString(1, clubName);
            pstmt.setString(2, position);
            pstmt.setString(3, applicant);

            // Execute the update query
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Applicant " + action + " successfully.");
            } else {
                System.out.println("Failed to " + action.toLowerCase() + " applicant. Check if the applicant, position, and club name match the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
