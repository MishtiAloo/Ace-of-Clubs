package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventItemController {

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
    private Button btn_EI_Register;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/A2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "an32bell206";

    private String eventName;


    public void setEventName(String eventName) {
        this.eventName = eventName;
        tv_EI_EventName.setText(eventName);
    }

    public void setClubName(String clubName) {
        tv_EI_ClubName.setText(clubName);
    }

    public void setPrograms(String programs) {
        tv_EI_Progs.setText("Programs: " + programs);
    }

    public void setStartDate(String startDate) {
        tv_EI_startDate.setText("Starting Date: " + startDate);
    }

    public void setEndDate(String endDate) {
        tv_EI_EndDate.setText("Ending Date: " + endDate);
    }


    public void checkIfRegistered(String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT `Events Participated` FROM p_users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String eventsParticipated = resultSet.getString("Events Participated");


                if (eventsParticipated != null && eventsParticipated.contains(eventName)) {
                    btn_EI_Register.setVisible(false);
                    Text alreadyRegisteredText = new Text("Already registered");
                    alreadyRegisteredText.setStyle("-fx-fill: white; -fx-font-size: 16px;");
                    ((AnchorPane) btn_EI_Register.getParent()).getChildren().add(alreadyRegisteredText);
                    alreadyRegisteredText.setLayoutX(btn_EI_Register.getLayoutX());
                    alreadyRegisteredText.setLayoutY(btn_EI_Register.getLayoutY() + 50);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void RegisterEvent(MouseEvent event) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE p_users SET `Events Participated` = CONCAT(IFNULL(`Events Participated`, ''), ?) WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            String eventToAdd = eventName + ",";
            statement.setString(1, eventToAdd);
            statement.setString(2, EventsPageController.globaluser);
            statement.executeUpdate();


            btn_EI_Register.setVisible(false);
            Text alreadyRegisteredText = new Text("Already registered");
            alreadyRegisteredText.setStyle("-fx-fill: white; -fx-font-size: 16px;");
            ((AnchorPane) btn_EI_Register.getParent()).getChildren().add(alreadyRegisteredText);
            alreadyRegisteredText.setLayoutX(btn_EI_Register.getLayoutX());
            alreadyRegisteredText.setLayoutY(btn_EI_Register.getLayoutY() + 50);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
