package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddRecruitmentPageController {

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    public static String globalclub;  // This should hold the club name

    @FXML
    private Button btn_finalizeRecruit;

    @FXML
    private ComboBox<String> cb_ARP_allowedBatch;

    @FXML
    private ComboBox<String> cb_ARP_position;

    @FXML
    private DatePicker dp_ARP_endDate;

    @FXML
    private TextField tf_ARP_avSlots;

    @FXML
    private Text tv_ARP_clubname;

    @FXML
    void goBack(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("MyClubsPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void finalizeRecruitment(MouseEvent event) {

        String clubName = globalclub;
        String position = cb_ARP_position.getValue();
        LocalDate endDate = dp_ARP_endDate.getValue();
        String allowedBatch = cb_ARP_allowedBatch.getValue();
        String availableSlots = tf_ARP_avSlots.getText();

        // Validation check
        if (position == null || endDate == null || allowedBatch == null || availableSlots.isEmpty()) {
            System.out.println("Please fill all fields");
            return;
        }

        insertRecruitment(clubName, position, endDate, availableSlots);
    }

    private void insertRecruitment(String clubName, String position, LocalDate endDate, String availableSlots) {

        String url = "jdbc:mysql://localhost:3306/A2";
        String user = "root";
        String password = "an32bell206";

        String sql = "INSERT INTO p_recruitments (`Club name`, Position, `Ending Date`, Slots) VALUES (?, ?, ?, ?)";

        try {

            Connection conn = DriverManager.getConnection(url, user, password);


            PreparedStatement pstmt = conn.prepareStatement(sql);


            pstmt.setString(1, clubName);
            pstmt.setString(2, position);
            pstmt.setString(3, endDate.toString());
            pstmt.setInt(4, Integer.parseInt(availableSlots));


            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Recruitment added successfully.");
            } else {
                System.out.println("Failed to add recruitment.");
            }

            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
