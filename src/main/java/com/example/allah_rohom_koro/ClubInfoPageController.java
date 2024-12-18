package com.example.allah_rohom_koro;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URL;
import java.util.ResourceBundle;

public class ClubInfoPageController implements Initializable {
    public static String globalClubType;

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private Button btn_EP_back;

    @FXML
    public VBox Vb_ItemHolder;

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

        String jsonUrl = "https://gist.githubusercontent.com/MishtiAloo/eb1212de1ad936c107a94a0f94de4673/raw/c1b0977af727e3ab0c8e4131b2de324e011ea4a1/test.json";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(jsonUrl)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

            JsonArray clubArray = null;
            switch (globalClubType) {
                case "Other":
                    clubArray = jsonObject.getAsJsonArray("Other");
                    break;
                case "Sports":
                    clubArray = jsonObject.getAsJsonArray("Sports");
                    break;
                case "Charity":
                    clubArray = jsonObject.getAsJsonArray("Charity");
                    break;
                case "Business":
                    clubArray = jsonObject.getAsJsonArray("Business");
                    break;
                case "Cultural":
                    clubArray = jsonObject.getAsJsonArray("Cultural");
                    break;
                case "Robotics":
                    clubArray = jsonObject.getAsJsonArray("Robotics");
                    break;
                case "Programming":
                    clubArray = jsonObject.getAsJsonArray("Programming");
                    break;
                default:
                    System.out.println("Invalid club type!");
            }


            if (clubArray != null) {
                for (int i = 0; i < clubArray.size(); i++) {
                    JsonObject clubObject = clubArray.get(i).getAsJsonObject();
                    addClubToUI(clubObject);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void addClubToUI(JsonObject clubObject) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClubInfoItem.fxml"));
            Parent clubInfoItem = loader.load();


            ClubInfoItemController controller = loader.getController();
            controller.setClubInfo(
                    clubObject.get("clubName").getAsString(),
                    clubObject.get("clubMotto").getAsString(),
                    clubObject.get("clubDetails").getAsString(),
                    formatMembers(clubObject.getAsJsonArray("members")),
                    formatPanels(clubObject.getAsJsonArray("panel"))
            );


            Vb_ItemHolder.getChildren().add(clubInfoItem);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String formatMembers(JsonArray members) {
        StringBuilder membersInfo = new StringBuilder();
        for (int i = 0; i < members.size(); i++) {
            JsonObject member = members.get(i).getAsJsonObject();
            membersInfo.append(member.get("name").getAsString())
                    .append(" (Roll: ").append(member.get("roll").getAsString())
                    .append(", Year: ").append(member.get("year").getAsString())
                    .append(", Dept: ").append(member.get("department").getAsString())
                    .append(")\n");
        }
        return membersInfo.toString();
    }


    private String formatPanels(JsonArray panels) {
        StringBuilder panelInfo = new StringBuilder();
        for (int i = 0; i < panels.size(); i++) {
            JsonObject panel = panels.get(i).getAsJsonObject();
            panelInfo.append(panel.get("name").getAsString())
                    .append(" (Roll: ").append(panel.get("roll").getAsString())
                    .append(", Year: ").append(panel.get("year").getAsString())
                    .append(", Dept: ").append(panel.get("department").getAsString())
                    .append(")\n");
        }
        return panelInfo.toString();
    }
}
