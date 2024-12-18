package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MyEventPageController implements Initializable {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private VBox Vb_EventItemHolder;

    public static String globaluser;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);


            String query = "SELECT * FROM p_users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, globaluser);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String userType = resultSet.getString("usertype");
                if (userType.equals("user")) {
                    String eventsParticipated = resultSet.getString("Events Participated");
                    List<String> eventList = Arrays.asList(eventsParticipated.split(","));

                    System.out.println("Events Participated: " + eventList);


                    for (String eventName : eventList) {
                        loadEventData(eventName, false, connection);
                    }

                } else if (userType.equals("admin")) {
                    String ownClub = resultSet.getString("ownclub");


                    String eventQuery = "SELECT * FROM p_events WHERE Club = ?";
                    PreparedStatement eventStmt = connection.prepareStatement(eventQuery);
                    eventStmt.setString(1, ownClub);
                    ResultSet eventResultSet = eventStmt.executeQuery();

                    while (eventResultSet.next()) {
                        String eventName = eventResultSet.getString("Event Name");
                        loadEventData(eventName, true, connection);
                    }
                }
            }


            resultSet.close();
            statement.close();

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEventData(String eventName, boolean isAdmin, Connection connection) {
        try {

            String eventQuery = "SELECT * FROM p_events WHERE `Event Name` = ?";
            PreparedStatement eventStmt = connection.prepareStatement(eventQuery);
            eventStmt.setString(1, eventName);
            ResultSet eventResultSet = eventStmt.executeQuery();

            if (eventResultSet.next()) {

                String clubName = eventResultSet.getString("Club");
                String startDate = eventResultSet.getString("Start Date");
                String endDate = eventResultSet.getString("End Date");
                String competitions = eventResultSet.getString("Competitions");


                FXMLLoader loader = new FXMLLoader(getClass().getResource("MyEventItem.fxml"));
                AnchorPane eventItem = loader.load();


                MyEventItemController controller = loader.getController();


                controller.setEventName(eventName);
                controller.setClubName(clubName);
                controller.setProgs(competitions);
                controller.setStartDate(startDate);
                controller.setEndDate(endDate);

                if (!isAdmin) {
                    controller.hideAdminButtons();
                }

                // Add the item to the VBox
                Vb_EventItemHolder.getChildren().add(eventItem);
            }

            // Close the result set
            eventResultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
