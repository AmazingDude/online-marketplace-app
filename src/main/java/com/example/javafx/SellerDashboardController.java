// SellerDashboardController.java
package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class SellerDashboardController {
    @FXML
    private ListView<Product> productList;

    private ObservableList<Product> observableProducts = FXCollections.observableArrayList();
    private MainViewController mainViewController;

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
        loadSellerProducts();
    }

    public void initialize() {
        productList.setItems(observableProducts);
    }

    // SellerDashboardController.java
    private void loadSellerProducts() {
        User loggedInUser = LoginController.getLoggedInUser();
        if (loggedInUser instanceof Seller) {
            observableProducts.setAll(((Seller) loggedInUser).getProductsForSale());
        } else {
            observableProducts.clear();
            System.out.println("Error: Not logged in as a seller.");
            // Optionally show an alert
        }
    }

    @FXML
    protected void removeSelectedProduct() {
        Product selectedProduct = productList.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            User loggedInUser = LoginController.getLoggedInUser();
            if (loggedInUser instanceof Seller && ((Seller) loggedInUser).getProductsForSale().contains(selectedProduct)) {
                ((Seller) loggedInUser).removeProduct(selectedProduct);
                MarketplaceData.getInstance().removeProduct(selectedProduct.getProductId()); // Remove from marketplace data
                loadSellerProducts(); // Refresh the list
                mainViewController.updateProductList(); // Update the main product list
            } else {
                System.out.println("Error: You cannot remove this product.");
                // Optionally show an alert
            }
        } else {
            System.out.println("Please select a product to remove.");
            // Optionally show an alert
        }
    }

    @FXML
    protected void closeDashboard() {
        Stage stage = (Stage) productList.getScene().getWindow();
        stage.close();
    }
}