package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;

public class ClubsPageController {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private VBox Vb_ClubItemHolder;

    @FXML
    private Button btn_CP_back;

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

        try {

            for (int i = 0; i < 10; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ClubItem.fxml"));
                Node clubItem = loader.load();

                Vb_ClubItemHolder.getChildren().add(clubItem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
