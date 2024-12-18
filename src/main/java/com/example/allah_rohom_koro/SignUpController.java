package com.example.allah_rohom_koro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SignUpController  {

    // Database credentials
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
    private Button btn_LoginPage_Login;

    @FXML
    private Button btn_LoginPage_SignUp;

    @FXML
    private Text tv_ForgotPass;

    @FXML
    void gotoPassResetPage(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ForgotPasswordPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void goBack(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private TextField tf_LP_username;

    @FXML
    private TextField tf_LP_pas;

    @FXML
    private Text tf_errorMessage;

    @FXML
    private Text tv_welcome_txt;

    public static String globalUsername;

    @FXML
    void gotoWelcomeUserPage(MouseEvent event) throws IOException {
        String username = tf_LP_username.getText();
        String password = tf_LP_pas.getText();


        tf_errorMessage.setVisible(false);

        if (username.isEmpty() || password.isEmpty()) {
            tf_errorMessage.setText("Username and Password must not be empty.");
            tf_errorMessage.setVisible(true);  // Show the error message
        } else {
            boolean isAuthenticated = login(username, password);
            if (isAuthenticated) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("WelcomeUserPage.fxml"));
                root = loader.load();


                SignUpController welcomeController = loader.getController();
                welcomeController.setWelcomeMessage(username);

                globalUsername = username;

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                tf_errorMessage.setText("Incorrect username/password.");
                tf_errorMessage.setVisible(true);  // Show the error message
            }
        }
    }


    private boolean login(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM P_Users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();  // Returns true if a record is found
        } catch (SQLException e) {
            tf_errorMessage.setText("Database error: " + e.getMessage());
            return false;
        }
    }


    public void setWelcomeMessage(String username) {
        if (tv_welcome_txt != null) {
            tv_welcome_txt.setText("Welcome " + username + "!!");
        }
    }


    @FXML
    void gotoSignUpPage(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SignUpPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private TextField tfUsernameA, tfEmailA, tvStudentIDA;
    @FXML
    private PasswordField pfPasswordA;
    @FXML
    private ComboBox<String> cbClubA, cbDepartmentA, cbYearA, cbSemesterA, cbBatchA, cbPositionA;


    @FXML
    private TextField tfUsername, tfEmail, tvStudentID;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private ComboBox<String> cbDepartment, cbYear, cbBatch, cbInterest;

    @FXML
    void gotoLoginPage(MouseEvent event) throws IOException {
        if (isAuthorityForm()) {

            if (validateAuthorityFields()) {

                String username = tfUsernameA.getText();
                String email = tfEmailA.getText();
                String password = pfPasswordA.getText();
                String studentID = tvStudentIDA.getText();
                String batch = cbBatchA.getValue();
                String dept = cbDepartmentA.getValue();
                String year = cbYearA.getValue();
                String club = cbClubA.getValue();
                String position = cbPositionA.getValue();

                // Basic validation
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || studentID.isEmpty() || batch == null || dept == null || year == null || club == null || position == null) {
                    return;
                }

                // Check if the username already exists
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String checkUserQuery = "SELECT COUNT(*) FROM P_Users WHERE username = ?";
                    PreparedStatement checkStatement = connection.prepareStatement(checkUserQuery);
                    checkStatement.setString(1, username);
                    ResultSet resultSet = checkStatement.executeQuery();

                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        return;
                    }


                    String insertQuery = "INSERT INTO P_Users (username, password, email, studentID, batch, year, dept, interest, usertype, ownclub, position) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'admin', ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(insertQuery);
                    statement.setString(1, username);
                    statement.setString(2, password);
                    statement.setString(3, email);
                    statement.setString(4, studentID);
                    statement.setString(5, batch);
                    statement.setString(6, year);
                    statement.setString(7, dept);
                    statement.setString(8, "none");  // You can add a field for 'interest' if needed
                    statement.setString(9, club);
                    statement.setString(10, position);

                    statement.executeUpdate();

                } catch (SQLException e) {

                }



                root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                System.out.println("Authority registration successful");
            }
        } else {

            if (validateUserFields()) {

                // Get user input from the form fields
                String username = tfUsername.getText();
                String email = tfEmail.getText();
                String password = pfPassword.getText();
                String studentID = tvStudentID.getText();
                String batch = cbBatch.getValue();
                String dept = cbDepartment.getValue();
                String year = cbYear.getValue();
                String interest = cbInterest.getValue();


                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || studentID.isEmpty() || batch == null || dept == null || year == null || interest == null) {

                    return;
                }


                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String checkUserQuery = "SELECT COUNT(*) FROM P_Users WHERE username = ?";
                    PreparedStatement checkStatement = connection.prepareStatement(checkUserQuery);
                    checkStatement.setString(1, username);
                    ResultSet resultSet = checkStatement.executeQuery();

                    if (resultSet.next() && resultSet.getInt(1) > 0) {

                        return;
                    }


                    String insertQuery = "INSERT INTO P_Users (username, password, email, studentID, batch, year, dept, interest, usertype) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'user')";
                    PreparedStatement statement = connection.prepareStatement(insertQuery);
                    statement.setString(1, username);
                    statement.setString(2, password);
                    statement.setString(3, email);
                    statement.setString(4, studentID);
                    statement.setString(5, batch);
                    statement.setString(6, year);
                    statement.setString(7, dept);
                    statement.setString(8, interest);

                    statement.executeUpdate();

                } catch (SQLException e) {

                }



                root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                System.out.println("User registration successful");
            }
        }
    }


    private boolean isAuthorityForm() {
        return tfUsernameA != null && tfEmailA != null && tvStudentIDA != null;
    }


    private boolean validateAuthorityFields() {
        if (tfUsernameA.getText().isEmpty() || tfEmailA.getText().isEmpty() || pfPasswordA.getText().isEmpty() ||
                tvStudentIDA.getText().isEmpty() || cbClubA.getValue() == null || cbDepartmentA.getValue() == null ||
                cbYearA.getValue() == null || cbSemesterA.getValue() == null || cbBatchA.getValue() == null ||
                cbPositionA.getValue() == null) {
            showError("All fields must be filled for Authority registration.");
            return false;
        }
        if (!isValidEmail(tfEmailA.getText())) {
            showError("Invalid email format for Authority registration.");
            return false;
        }
        return true;
    }


    private boolean validateUserFields() {
            if (tfUsername.getText().isEmpty() || tfEmail.getText().isEmpty() || pfPassword.getText().isEmpty() ||
                tvStudentID.getText().isEmpty() || cbDepartment.getValue() == null || cbYear.getValue() == null ||
                cbBatch.getValue() == null || cbInterest.getValue() == null) {
            showError("All fields must be filled for User registration.");
            return false;
        }
        if (!isValidEmail(tfEmail.getText())) {
            showError("Invalid email format for User registration.");
            return false;
        }
        return true;
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }


    private void showError(String message) {
        System.out.println(message);
    }

    @FXML
    void gotoAuthorityRegPage(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AuthorityRegPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void gotoVisitorRegPage(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("UserRegPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Button btn_imgUp;

    @FXML
    private ImageView iv_SignUpPage_Auth_UploadedImageHolder;

    @FXML
    public void handleImageUpload() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");


        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));


        Stage stage = (Stage) btn_imgUp.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);


        if (selectedFile != null) {

            Image image = new Image(selectedFile.toURI().toString());
            iv_SignUpPage_Auth_UploadedImageHolder.setImage(image);
        }
    }

    @FXML
    void gotoHomePage(MouseEvent event) throws IOException {
        HomePageController.globalusername = globalUsername;
        root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextField FP_email;

    @FXML
    private TextField FP_newpass;

    @FXML
    void resetPass(MouseEvent event) throws IOException {
        // Get input values
        String email = FP_email.getText();
        String newPassword = FP_newpass.getText();

        // Check if fields are empty
        if (email.isEmpty() || newPassword.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }


        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            showAlert("Invalid email format.");
            return;
        }


        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            String query = "SELECT COUNT(*) FROM p_users WHERE email = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // Email exists, update the password
                    String updateQuery = "UPDATE p_users SET password = ? WHERE email = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, newPassword); // In a real-world scenario, hash the password
                        updateStmt.setString(2, email);
                        int rowsUpdated = updateStmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            showAlert("Password successfully reset.");
                        } else {
                            showAlert("Failed to reset password.");
                        }
                    }
                } else {
                    showAlert("Email not found in the database.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("An error occurred while processing your request.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to connect to the database.");
        }

        root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
