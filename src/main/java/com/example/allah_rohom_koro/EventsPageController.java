package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventsPageController {

    public static String globaluser;

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private VBox Vb_EventItemHolder;

    @FXML
    private Button btn_EP_back;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/A2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "an32bell206";

    @FXML
    void goBack(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT `Event Name`, `Start Date`, `End Date`, `Competitions`, `Club` FROM p_events";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String eventName = resultSet.getString("Event Name");
                String startDate = resultSet.getString("Start Date");
                String endDate = resultSet.getString("End Date");
                String competitions = resultSet.getString("Competitions");
                String clubName = resultSet.getString("Club");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("EventItem.fxml"));
                Node eventItem = loader.load();

                EventItemController eventItemController = loader.getController();
                eventItemController.setEventName(eventName);
                eventItemController.setClubName(clubName);
                eventItemController.setPrograms(competitions);
                eventItemController.setStartDate(startDate);
                eventItemController.setEndDate(endDate);

                eventItemController.checkIfRegistered(globaluser);

                Vb_EventItemHolder.getChildren().add(eventItem);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
