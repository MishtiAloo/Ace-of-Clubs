package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ClubInfoItemController {
    @FXML
    private Text tv_CII_clubDetails;

    @FXML
    private Text tv_CII_clubMotto;

    @FXML
    private Text tv_CII_clubName;

    @FXML
    private Text tv_CII_members;

    @FXML
    private Text tv_CII_panels;

    public void setClubInfo(String clubName, String clubMotto, String clubDetails, String members, String panels) {
        tv_CII_clubName.setText(clubName);
        tv_CII_clubMotto.setText(clubMotto);
        tv_CII_clubDetails.setText(clubDetails);
        tv_CII_members.setText(members);
        tv_CII_panels.setText(panels);
    }
}
