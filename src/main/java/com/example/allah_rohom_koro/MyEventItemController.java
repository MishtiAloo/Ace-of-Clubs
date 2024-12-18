package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class MyEventItemController {

    @FXML
    private Text tv_EI_EventName;
    @FXML
    private Text tv_EI_ClubName;
    @FXML
    private Text tv_EI_Progs;
    @FXML
    private Text tv_EI_startDate;
    @FXML
    private Text tv_EI_EndDate;

    @FXML
    private Button btn_MEI_reschedule;
    @FXML
    private Button btn_MEI_cancel;

    private String dbUrl = "jdbc:mysql://localhost:3306/A2";
    private String dbUsername = "root";
    private String dbPassword = "an32bell206";

    // Setters for event data
    public void setEventName(String eventName) {
        tv_EI_EventName.setText(eventName);
    }

    public void setClubName(String clubName) {
        tv_EI_ClubName.setText(clubName);
    }

    public void setProgs(String competitions) {
        tv_EI_Progs.setText(competitions);
    }

    public void setStartDate(String startDate) {
        tv_EI_startDate.setText("Starting Date: " + startDate);
    }

    public void setEndDate(String endDate) {
        tv_EI_EndDate.setText("Ending Date: " + endDate);
    }

    public void hideAdminButtons() {
        btn_MEI_reschedule.setVisible(false);
        btn_MEI_cancel.setVisible(false);
    }


    @FXML
    public void onCancelClicked(MouseEvent event) {
        String eventName = tv_EI_EventName.getText();
        deleteEventFromDatabase(eventName);
        deleteUserEventParticipation(eventName);
    }


    @FXML
    public void onRescheduleClicked(MouseEvent event) {
        showReschedulePrompt();
    }


    private void deleteEventFromDatabase(String eventName) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String deleteEventQuery = "DELETE FROM p_events WHERE `Event Name` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteEventQuery);
            preparedStatement.setString(1, eventName);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Event deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteUserEventParticipation(String eventName) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String updateUsersQuery = "UPDATE p_users SET `Events Participated` = REPLACE(`Events Participated`, ?, '')";
            PreparedStatement preparedStatement = connection.prepareStatement(updateUsersQuery);
            preparedStatement.setString(1, eventName + ", ");
            preparedStatement.executeUpdate();
            System.out.println("Event removed from users' participation.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void showReschedulePrompt() {
        DatePicker newStartDatePicker = new DatePicker();
        DatePicker newEndDatePicker = new DatePicker();


        Optional<String> newStartDate = Optional.ofNullable(newStartDatePicker.getValue().toString());
        Optional<String> newEndDate = Optional.ofNullable(newEndDatePicker.getValue().toString());

        if (newStartDate.isPresent() && newEndDate.isPresent()) {
            updateEventDates(tv_EI_EventName.getText(), newStartDate.get(), newEndDate.get());
        }
    }


    private void updateEventDates(String eventName, String newStartDate, String newEndDate) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String updateEventQuery = "UPDATE p_events SET `Start Date` = ?, `End Date` = ? WHERE `Event Name` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateEventQuery);
            preparedStatement.setString(1, newStartDate);
            preparedStatement.setString(2, newEndDate);
            preparedStatement.setString(3, eventName);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Event dates updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
