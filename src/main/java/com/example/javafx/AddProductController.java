// AddProductController.java
package com.example.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class AddProductController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField priceField;

    private MainViewController mainViewController;

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    @FXML
    protected void addProduct() {
        String name = nameField.getText();
        String description = descriptionField.getText();
        try {
            double price = Double.parseDouble(priceField.getText());
            User currentUser = LoginController.getLoggedInUser();
            if (currentUser instanceof Seller) {
                Seller seller = (Seller) currentUser;
                Product newProduct = new Product(0, name, description, price, seller);
                MarketplaceData.getInstance().addProduct(newProduct); // Add to the main marketplace list
                seller.addProduct(newProduct); // Add to the seller's listed products
                mainViewController.updateProductList(); // Update the main list in the UI
                closeDialog();
            } else {
                System.out.println("Error: Current user is not a seller.");
                // Show an error message to the user
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format.");
            // Show an error message to the user
        }
    }

    @FXML
    protected void cancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}