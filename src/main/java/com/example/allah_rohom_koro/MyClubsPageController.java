package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class MyClubsPageController {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    public static String panel;

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
    private VBox Vb_MyClubItemHolder;

    @FXML
    public void initialize() {
        try {

            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "SELECT `Club Name`, `Club motto`, `Club Type` FROM p_clubs WHERE FIND_IN_SET(?, `Panel List`)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, panel);
            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("MyClubsItem.fxml"));
                Node clubItem = loader.load();


                MyClubsItemController itemController = loader.getController();
                itemController.setClubName(resultSet.getString("Club Name"));
                itemController.setClubMotto(resultSet.getString("Club motto"));
                itemController.setClubType(resultSet.getString("Club Type"));


                Vb_MyClubItemHolder.getChildren().add(clubItem);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
