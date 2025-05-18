// LoginController.java
package com.example.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.scene.Node;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;
    @FXML
    private RadioButton buyerRadioButton;
    @FXML
    private RadioButton sellerRadioButton;
    @FXML
    private ToggleGroup accountTypeGroup;

    private MainViewController mainViewController;
    private static User loggedInUser = null;

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    @FXML
    protected void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = MarketplaceData.getInstance().authenticateUser(username, password);
        if (user != null) {
            loggedInUser = user;
            messageLabel.setText("Login successful!");
            closeDialog();
            mainViewController.updateProductList();
        } else {
            messageLabel.setText("Invalid credentials.");
        }
    }

    @FXML
    protected void register() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = "default@example.com"; // You might want to add an email field later

        // Basic validation
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            messageLabel.setText("Username and Password cannot be empty");
            return;
        }

        if (MarketplaceData.getInstance().findUserByUsername(username) == null) {
            User newUser = null;
            if (buyerRadioButton.isSelected()) {
                newUser = new Buyer(0, username, password, email);
                messageLabel.setText("Registration successful! Logged in as Buyer.");
            } else if (sellerRadioButton.isSelected()) {
                newUser = new Seller(0, username, password, email);
                messageLabel.setText("Registration successful! Logged in as Seller.");
            } else {
                messageLabel.setText("Please select an account type.");
                return;
            }

            if (newUser != null) {
                MarketplaceData.getInstance().addUser(newUser);
                loggedInUser = newUser; // Log in the new user
                closeDialog();
                mainViewController.updateProductList();
            }
        } else {
            messageLabel.setText("Username already exists.");
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}