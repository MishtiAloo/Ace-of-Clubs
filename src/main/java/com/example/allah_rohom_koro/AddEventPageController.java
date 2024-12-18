package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEventPageController implements Initializable {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    public static String globalClub;

    @FXML
    private Text tv_AEP_clubname;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/A2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "an32bell206";

    @FXML
    private TextField tf_AEP_eventName;

    @FXML
    private DatePicker dp_AEP_startDate;

    @FXML
    private DatePicker dp_AEP_endDate;

    @FXML
    private CheckBox cb_AEP_comp1;

    @FXML
    private CheckBox cb_AEP_comp2;

    @FXML
    private CheckBox cb_AEP_comp3;

    @FXML
    private CheckBox cb_AEP_comp4;

    @FXML
    private ComboBox<String> cb_AEP_allowedBatch;

    @FXML
    private Button btn_AEP_finalizeEvent;

    @FXML
    void goBack(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("MyClubsPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void finalizeEvent(ActionEvent event) {
        String eventName = tf_AEP_eventName.getText();
        String startDate = dp_AEP_startDate.getValue() != null ? dp_AEP_startDate.getValue().toString() : "";
        String endDate = dp_AEP_endDate.getValue() != null ? dp_AEP_endDate.getValue().toString() : "";


        List<String> selectedCompetitions = new ArrayList<>();
        if (cb_AEP_comp1.isSelected()) selectedCompetitions.add(cb_AEP_comp1.getText());
        if (cb_AEP_comp2.isSelected()) selectedCompetitions.add(cb_AEP_comp2.getText());
        if (cb_AEP_comp3.isSelected()) selectedCompetitions.add(cb_AEP_comp3.getText());
        if (cb_AEP_comp4.isSelected()) selectedCompetitions.add(cb_AEP_comp4.getText());


        String allowedBatch = cb_AEP_allowedBatch.getValue();


        if (eventName.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || selectedCompetitions.isEmpty() || allowedBatch == null) {
            // Show an error alert if validation fails
            showAlert("Please fill all fields and select at least one competition.");
            return;
        }


        String competitions = String.join(", ", selectedCompetitions);


        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO p_events (`Event Name`, `Start Date`, `End Date`, `Competitions`, `Allowed batch`, `Club`) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, eventName);
            statement.setString(2, startDate);
            statement.setString(3, endDate);
            statement.setString(4, competitions);
            statement.setString(5, allowedBatch);
            statement.setString(6, globalClub);
            statement.executeUpdate();
            showAlert("Event added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error occurred while adding the event.");
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Display the club name from globalClub
        tv_AEP_clubname.setText("Event under the banner of " + globalClub);
    }
}
