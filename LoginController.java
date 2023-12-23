package com.example.pbouas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;


    private static final String DB_URL = "jdbc:mysql://localhost/elab";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authenticateUser(username, password)) {
            System.out.println("Login Successful");
            redirectToDashboard();
        } else {
            System.out.println("Login Failed");
            showAlert("Gagal Login", "LOGIN FAILED!");
        }
    }
    private void loadNewScene(String fxmlFileName, ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent newSceneParent = null;
        try {
            newSceneParent = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Membuat scene baru
        Scene newScene = new Scene(newSceneParent);

        // Mendapatkan informasi stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Menetapkan scene baru pada stage
        stage.setScene(newScene);

        // Menampilkan stage dengan scene baru
        stage.show();
    }

    @FXML
    private void handleSignUpButton(ActionEvent event) {
        loadNewScene("daftar.fxml", event);

    }

    private boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM user WHERE Nim = ? AND Pass = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Jika ada hasil, user ada dalam database
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void redirectToDashboard() {
        try {
            // Load halaman dashboard.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = loader.load();

            // Set scene untuk halaman dashboard
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginButton.getScene().getWindow(); // Mendapatkan stage dari tombol login
            stage.setScene(scene);

            // Menunjukkan stage baru (dashboard)
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
