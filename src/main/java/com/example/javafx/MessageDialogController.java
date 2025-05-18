// MessageDialogController.java
package com.example.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

public class MessageDialogController {

    @FXML
    private Label productInfoLabel;

    @FXML
    private TextArea messageTextArea;

    private MainViewController mainViewController;
    private Product product;

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            productInfoLabel.setText("Contacting seller for: " + product.getName());
        } else {
            productInfoLabel.setText("No product selected.");
        }
    }

    @FXML
    protected void sendMessage() {
        if (product != null && LoginController.getLoggedInUser() != null) {
            User sender = LoginController.getLoggedInUser();
            Seller receiver = product.getSeller();
            String content = messageTextArea.getText();

            if (receiver != null) {
                Message newMessage = new Message(0, sender.getUserId(), receiver.getUserId(), product.getProductId(), content);
                MarketplaceData.getInstance().addMessage(newMessage); // Call addMessage here
                System.out.println("Message sent to " + receiver.getUsername() + " regarding " + product.getName());
                closeDialog();
            } else {
                System.out.println("Error: Product has no seller.");
                // Optionally show an alert
            }
        } else {
            System.out.println("Error: Not logged in or no product selected.");
            // Optionally show an alert
        }
    }

    @FXML
    protected void cancelMessage() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) messageTextArea.getScene().getWindow();
        stage.close();
    }
}