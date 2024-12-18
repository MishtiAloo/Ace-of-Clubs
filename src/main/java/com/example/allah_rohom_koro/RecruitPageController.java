package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RecruitPageController {

    String url = "jdbc:mysql://localhost:3306/A2";  // Replace with your DB details
    String user = "root";  // Replace with your DB username
    String password = "an32bell206";  // Replace with your DB password

    public static String globaluser;

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private VBox Vb_RecruitItemHolder;

    @FXML
    private Button btn_RP_back;

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
        // SQL SELECT query to fetch recruitment data
        String sql = "SELECT `Club name`, Position, `Ending Date`, Slots, Waiting FROM p_recruitments";
        String userTypeQuery = "SELECT usertype FROM p_users WHERE username = ?"; // Query to check usertype

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Check the usertype of the globaluser
            String userType = null;
            try (PreparedStatement userTypeStmt = conn.prepareStatement(userTypeQuery)) {
                userTypeStmt.setString(1, globaluser);
                ResultSet userTypeRs = userTypeStmt.executeQuery();
                if (userTypeRs.next()) {
                    userType = userTypeRs.getString("usertype");
                }
            }

            // Loop through the result set and process each recruitment
            while (rs.next()) {
                String clubName = rs.getString("Club name");
                String position = rs.getString("Position");
                String endingDate = rs.getString("Ending Date");
                int slots = rs.getInt("Slots");
                String waiting = rs.getString("Waiting");

                // Check if the user is already in the Waiting list
                boolean isApplied = waiting != null && waiting.contains(globaluser);
                // Check if the user is an admin
                boolean isAdmin = "admin".equalsIgnoreCase(userType);

                // Load the RecruitmentItem FXML for each recruitment entry
                FXMLLoader loader = new FXMLLoader(getClass().getResource("RecruitmentItem.fxml"));
                Node recruitmentItem = loader.load();

                // Get the controller of the loaded RecruitmentItem
                RecruitmentItemController controller = loader.getController();
                RecruitmentItemController.globaluser = globaluser;

                // Set the data and apply button visibility in the RecruitmentItem
                controller.setRecruitmentData(clubName, position, endingDate, slots, isApplied, isAdmin);

                // Add the item to the VBox
                Vb_RecruitItemHolder.getChildren().add(recruitmentItem);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


}
