// MainViewController.java
package com.example.javafx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import javafx.scene.Parent;

public class MainViewController {
    @FXML
    private MenuItem inboxMenuItem;

    private Stage inboxStage;
    @FXML
    private ListView<Product> productList;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button buyButton;
    @FXML
    private Button contactSellerButton; // Added for the Contact Seller button

    private ObservableList<Product> observableProducts = FXCollections.observableArrayList();
    private MarketplaceData data = MarketplaceData.getInstance();
    private Product selectedProduct;

    private Stage addProductStage;
    private Stage loginStage;
    private Stage reportsStage;
    private Stage sellerDashboardStage;
    private Stage messageStage; // Add for message window

    public void initialize() {
        observableProducts.addAll(data.getAllProducts());
        productList.setItems(observableProducts);
        buyButton.setDisable(true);
        contactSellerButton.setDisable(true); // Initially disable the Contact Seller button

        productList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedProduct = newSelection;
                boolean isOwnProduct = LoginController.getLoggedInUser() instanceof Seller &&
                        ((Seller) LoginController.getLoggedInUser()).getProductsForSale().contains(selectedProduct);
                buyButton.setDisable(isOwnProduct || LoginController.getLoggedInUser() == null || !(LoginController.getLoggedInUser() instanceof Buyer));
                contactSellerButton.setDisable(isOwnProduct || LoginController.getLoggedInUser() == null || !(LoginController.getLoggedInUser() instanceof Buyer));
            } else {
                selectedProduct = null;
                buyButton.setDisable(true);
                contactSellerButton.setDisable(true);
            }
        });
    }

    @FXML
    protected void exitApplication() {
        Platform.exit();
    }

    @FXML
    protected void openSellerDashboard() {
        User loggedInUser = LoginController.getLoggedInUser();
        if (loggedInUser instanceof Seller) {
            if (sellerDashboardStage == null) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MarketplaceApp.class.getResource("seller-dashboard-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                    sellerDashboardStage = new Stage();
                    sellerDashboardStage.setTitle("Seller Dashboard");
                    sellerDashboardStage.initModality(Modality.APPLICATION_MODAL);
                    sellerDashboardStage.setScene(scene);

                    SellerDashboardController controller = fxmlLoader.getController();
                    controller.setMainViewController(this);
                    sellerDashboardStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                sellerDashboardStage.show();
            }
        } else {
            System.out.println("You must be logged in as a seller to access the dashboard.");
            // Optionally show an alert
        }
    }

    @FXML
    protected void openInbox() {
        User loggedInUser = LoginController.getLoggedInUser();
        if (loggedInUser != null) {
            if (inboxStage == null) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MarketplaceApp.class.getResource("inbox-view.fxml"));
                    Parent root = fxmlLoader.load();
                    InboxController controller = fxmlLoader.getController();
                    controller.loadMessages(loggedInUser.getUserId()); // Use the current logged-in user's ID
                    Scene scene = new Scene(root, 600, 400);
                    inboxStage = new Stage();
                    inboxStage.setTitle("Inbox");
                    inboxStage.initModality(Modality.APPLICATION_MODAL);
                    inboxStage.setScene(scene);
                    inboxStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MarketplaceApp.class.getResource("inbox-view.fxml"));
                    Parent root = fxmlLoader.load();
                    InboxController controller = fxmlLoader.getController();
                    controller.loadMessages(loggedInUser.getUserId()); // Use the current logged-in user's ID
                    inboxStage.setScene(new Scene(root, 600, 400)); // Update the scene if needed
                    inboxStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("You must be logged in to view your inbox.");
            // Optionally show an alert
        }
    }

    @FXML
    protected void openAddProductDialog() {
        if (LoginController.getLoggedInUser() instanceof Seller) {
            if (addProductStage == null) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MarketplaceApp.class.getResource("add-product-dialog.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 400, 300);
                    addProductStage = new Stage();
                    addProductStage.setTitle("Add Product");
                    addProductStage.initModality(Modality.APPLICATION_MODAL);
                    addProductStage.setScene(scene);

                    AddProductController controller = fxmlLoader.getController();
                    controller.setMainViewController(this); // Pass a reference to the main controller
                    addProductStage.show();
                } catch (IOException e) {
                    e.printStackTrace(); // Consider showing an error message to the user
                }
            } else {
                addProductStage.show();
            }
        } else {
            // Inform the user they need to be logged in as a seller
            System.out.println("You must be logged in as a seller to add products.");
            // You could display an alert here
        }
    }

    @FXML
    protected void openReportsView() {
        if (reportsStage == null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MarketplaceApp.class.getResource("reports-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                reportsStage = new Stage();
                reportsStage.setTitle("Reports");
                reportsStage.initModality(Modality.APPLICATION_MODAL);
                reportsStage.setScene(scene);

                ReportsViewController controller = fxmlLoader.getController();
                controller.setMainViewController(this);
                reportsStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            reportsStage.show();
        }
    }

    @FXML
    protected void openLoginDialog() {
        if (loginStage == null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MarketplaceApp.class.getResource("login-dialog.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 400, 300);
                loginStage = new Stage();
                loginStage.setTitle("Login / Register");
                loginStage.initModality(Modality.APPLICATION_MODAL);
                loginStage.setScene(scene);

                LoginController controller = fxmlLoader.getController();
                controller.setMainViewController(this);
                loginStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loginStage.show();
        }
    }

    @FXML
    protected void searchProducts() {
        String query = searchTextField.getText();
        if (query != null && !query.isEmpty()) {
            observableProducts.setAll(data.searchProducts(query));
        } else {
            observableProducts.setAll(data.getAllProducts());
        }
    }

    public void updateProductList() {
        observableProducts.setAll(data.getAllProducts());
    }

    @FXML
    protected void handleProductSelection(MouseEvent event) {
        // The listener on productList.getSelectionModel().selectedItemProperty() handles this now.
    }

    @FXML
    protected void buySelectedItem() {
        if (selectedProduct != null) {
            User loggedInUser = LoginController.getLoggedInUser();
            if (loggedInUser instanceof Buyer) {
                Buyer buyer = (Buyer) loggedInUser;
                Seller seller = selectedProduct.getSeller();

                // Prevent buying your own products
                if (buyer.getUserId() == seller.getUserId()) {
                    System.out.println("You cannot buy your own product.");
                    return;
                }

                // Create a new transaction (now implicitly completed)
                Transaction transaction = new Transaction(0, buyer, seller, selectedProduct);
                MarketplaceData.getInstance().addTransaction(transaction);
                System.out.println(buyer.getUsername() + " completed purchase of " + selectedProduct.getName() + " from " + seller.getUsername());
                updateProductList();
            } else {
                System.out.println("You must be logged in as a buyer to purchase products.");
            }
        }
    }

    // Method to open the message window
    @FXML
    protected void openMessageDialog(Product product) {
        User loggedInUser = LoginController.getLoggedInUser();
        if (loggedInUser != null) {
            if (messageStage == null) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(MarketplaceApp.class.getResource("message-dialog.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 400, 300);
                    messageStage = new Stage();
                    messageStage.setTitle("Contact Seller");
                    messageStage.initModality(Modality.APPLICATION_MODAL);
                    messageStage.setScene(scene);
                    MessageDialogController controller = fxmlLoader.getController();
                    controller.setMainViewController(this);  // Pass the main controller
                    controller.setProduct(product); // Pass product
                    messageStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(MarketplaceApp.class.getResource("message-dialog.fxml"));
                try {
                    fxmlLoader.load();
                    MessageDialogController controller = fxmlLoader.getController();
                    controller.setProduct(product);
                    messageStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            System.out.println("You must be logged in to contact the seller");
        }
    }

    @FXML
    protected void handleContactSeller() {
        if (selectedProduct != null) {
            openMessageDialog(selectedProduct);
        } else {
            System.out.println("Please select a product to contact the seller.");
            // Optionally show an alert
        }
    }
}