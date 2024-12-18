package com.example.allah_rohom_koro;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/A2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "an32bell206";

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private VBox Vb_MyItemHolder;
    @FXML
    private Text tv_HP_Title;

    public static String globalusername;

    @FXML
    private AnchorPane ancpane_AHP_drawer;

    @FXML
    private AnchorPane ancpane_AHP_clubs;

    private boolean isDrawerOpen = false;

    @FXML
    private void invokeSlider() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.5));
        slide.setNode(ancpane_AHP_drawer);


        if (!isDrawerOpen) {

            slide.setToX(336);
            isDrawerOpen = true;
        } else {

            slide.setToX(0);
            isDrawerOpen = false;
        }

        slide.play();
    }

    @FXML
    void setClubsVisible(MouseEvent event) {
        ancpane_AHP_clubs.setVisible(true);
    }

    @FXML
    void setClubsInvisible(MouseEvent event) {
        ancpane_AHP_clubs.setVisible(false);
    }

    @FXML
    void keepClubsVisible(MouseEvent event) { ancpane_AHP_clubs.setVisible(true); }

    @FXML
    void makeClubsInvisible(MouseEvent event) { ancpane_AHP_clubs.setVisible(false); }

    @FXML
    void gotoMyClubPage(MouseEvent event) throws IOException {
        MyClubsPageController.panel = globalusername;
        root = FXMLLoader.load(getClass().getResource("MyClubsPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void gotoEventPage(MouseEvent event) throws IOException {
        EventsPageController.globaluser = globalusername;
        root = FXMLLoader.load(getClass().getResource("EventsPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void gotoProgramPage(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ProgramsPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void gotoRecruitPage(MouseEvent event) throws IOException {
        RecruitPageController.globaluser = globalusername;
        root = FXMLLoader.load(getClass().getResource("RecruitPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button btn_AHP_myProfile;

    @FXML
    void gotoMyProfilePage(MouseEvent event) throws IOException {
        MyprofilePageController.globalUser = globalusername;
        root = FXMLLoader.load(getClass().getResource("MyProfilePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void gotoMyEventsPage (MouseEvent event) throws IOException {
        MyEventPageController.globaluser = globalusername;
        root = FXMLLoader.load(getClass().getResource("MyEventPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button btn_AHP_myClubs;


    private void checkUserType(String username) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT usertype, interest FROM P_Users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String userType = resultSet.getString("usertype");
                String interest = resultSet.getString("interest");


                if ("admin".equalsIgnoreCase(userType)) {

                    btn_AHP_myClubs.setText("My Clubs");
                    tv_HP_Title.setText("Awaiting Applications");
                    loadAdminRecruitmentItems(globalusername);
                } else if ("user".equalsIgnoreCase(userType)) {

                    btn_AHP_myClubs.setText("My Worksheet");
                    tv_HP_Title.setText("Suggested Events");
                    loadUserEvents(globalusername, interest);
                }
            } else {
                System.out.println("User type not found for user: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUserEvents(String username, String interest) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            String clubQuery = "SELECT `Club Name` FROM p_clubs WHERE `Club Type` = ?";
            PreparedStatement clubStatement = connection.prepareStatement(clubQuery);
            clubStatement.setString(1, interest);
            ResultSet clubResultSet = clubStatement.executeQuery();


            while (clubResultSet.next()) {
                String clubName = clubResultSet.getString("Club Name");


                String eventQuery = "SELECT `Event Name`, `Competitions`, `Start Date`, `End Date` FROM p_events WHERE `Club` = ?";
                PreparedStatement eventStatement = connection.prepareStatement(eventQuery);
                eventStatement.setString(1, clubName);
                ResultSet eventResultSet = eventStatement.executeQuery();


                while (eventResultSet.next()) {
                    String eventName = eventResultSet.getString("Event Name");
                    String programs = eventResultSet.getString("Competitions");
                    String startDate = eventResultSet.getString("Start Date");
                    String endDate = eventResultSet.getString("End Date");


                    FXMLLoader loader = new FXMLLoader(getClass().getResource("EventItem.fxml"));
                    Parent eventItem = loader.load();


                    EventItemController eventItemController = loader.getController();
                    eventItemController.setEventName(eventName);
                    eventItemController.setClubName(clubName);
                    eventItemController.setPrograms(programs);
                    eventItemController.setStartDate(startDate);
                    eventItemController.setEndDate(endDate);


                    Vb_MyItemHolder.getChildren().add(eventItem);


                    eventItemController.checkIfRegistered(username);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gotoMyWorksheetPage(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("MyWorkSheetPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    private void loadAdminRecruitmentItems(String username) {
        // Step 1: Get admin's own club from the p_users table
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String clubQuery = "SELECT ownclub FROM p_users WHERE username = ?";
            PreparedStatement clubStatement = connection.prepareStatement(clubQuery);
            clubStatement.setString(1, username);
            ResultSet clubResultSet = clubStatement.executeQuery();

            if (clubResultSet.next()) {
                String adminClub = clubResultSet.getString("ownclub");

                if (adminClub == null || adminClub.isEmpty()) {
                    System.out.println("Admin does not own any club.");
                    return;
                }


                String recruitQuery = "SELECT Position, `Ending Date`, Waiting FROM p_recruitments WHERE `Club name` = ?";
                PreparedStatement recruitStatement = connection.prepareStatement(recruitQuery);
                recruitStatement.setString(1, adminClub);
                ResultSet recruitResultSet = recruitStatement.executeQuery();


                while (recruitResultSet.next()) {
                    String position = recruitResultSet.getString("Position");
                    String endDate = recruitResultSet.getString("Ending Date");
                    String waitingApplicants = recruitResultSet.getString("Waiting");

                    if (waitingApplicants == null || waitingApplicants.isEmpty()) {
                        System.out.println("No waiting applicants for position: " + position);
                        continue;
                    }


                    String[] applicants = waitingApplicants.split(",");

                    for (String applicant : applicants) {

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("WaitingRecruitItem.fxml"));
                        Parent recruitItem = loader.load();


                        WaitingRecruitItemController recruitItemController = loader.getController();
                        recruitItemController.setClubName(adminClub);
                        recruitItemController.setPosition(position);
                        recruitItemController.setApplicantName(applicant.trim());
                        recruitItemController.setEndingDate(endDate);


                        Vb_MyItemHolder.getChildren().add(recruitItem);
                    }
                }
            } else {
                System.out.println("Admin club not found for user: " + username);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private VBox Vb_ClubItemHolder;

    @FXML
    private Text tv_AHP_globalusername;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (ancpane_AHP_drawer != null) {
            ancpane_AHP_drawer.setTranslateX(-336);
        } else {
            System.out.println("ancpane_AHP_drawer is null!");
        }

        tv_AHP_globalusername.setText(globalusername);

        if (globalusername != null && !globalusername.isEmpty()) {
            tv_AHP_globalusername.setText(globalusername);
            checkUserType(globalusername);
        } else {
            tv_AHP_globalusername.setText("No user logged in");
        }

    }

    @FXML
    public Text charityClubs;

    @FXML
    public Text culturalClubs;

    @FXML
    public Text otherClubs;

    @FXML
    public Text programmingClubs;

    @FXML
    public Text roboticsClubs;

    @FXML
    public Text sportClubs;

    @FXML
    private Text businessClubs;

    @FXML
    void gotoClucInfoPage(MouseEvent event) throws IOException {

        if (event.getSource() == charityClubs) {
            ClubInfoPageController.globalClubType = charityClubs.getText();
        } else if (event.getSource() == culturalClubs) {
            ClubInfoPageController.globalClubType = culturalClubs.getText();
        } else if (event.getSource() == otherClubs) {
            ClubInfoPageController.globalClubType = otherClubs.getText();
        } else if (event.getSource() == programmingClubs) {
            ClubInfoPageController.globalClubType = programmingClubs.getText();
        } else if (event.getSource() == roboticsClubs) {
            ClubInfoPageController.globalClubType = roboticsClubs.getText();
        } else if (event.getSource() == businessClubs) {
            ClubInfoPageController.globalClubType = businessClubs.getText();
        } else if (event.getSource() == sportClubs) {
            ClubInfoPageController.globalClubType = sportClubs.getText();
        }

        root = FXMLLoader.load(getClass().getResource("ClubInfoPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
