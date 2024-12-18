package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;

public class MyClubsItemController {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private Text tv_clubItem_clubName;
    @FXML
    private Text tv_ClubItem_clubType;
    @FXML
    private Text tv_ClubItem_motto;


    public void setClubName(String clubName) {
        tv_clubItem_clubName.setText(clubName);
    }

    public void setClubType(String clubType) {
        tv_ClubItem_clubType.setText(clubType);
    }

    public void setClubMotto(String clubMotto) {
        tv_ClubItem_motto.setText(clubMotto);
    }

    @FXML
    private Button btn_MCI_addEvent;

    @FXML
    void gotoAddEventPage(MouseEvent event) throws IOException {
        AddEventPageController.globalClub = tv_clubItem_clubName.getText();
        root = FXMLLoader.load(getClass().getResource("AddEventPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void gotoAddRecruitPage(MouseEvent event) throws IOException {
        AddRecruitmentPageController.globalclub = tv_clubItem_clubName.getText();
        root = FXMLLoader.load(getClass().getResource("AddRecruitmentPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
