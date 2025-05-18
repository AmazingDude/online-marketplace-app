package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;

public class ReportsViewController {
    @FXML
    private ListView<Product> allProductsList;
    @FXML
    private ListView<User> allUsersList;

    @FXML
    private ListView<Transaction> allTransactionsList;
    @FXML
    private Label totalRevenueLabel;

    private MarketplaceData data = MarketplaceData.getInstance();
    private MainViewController mainViewController;

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void initialize() {
        // You could generate initial empty lists or a message
        allProductsList.setItems(FXCollections.observableArrayList());
        allUsersList.setItems(FXCollections.observableArrayList());
        allTransactionsList.setItems(FXCollections.observableArrayList());
        totalRevenueLabel.setText("");
    }

    @FXML
    protected void generateReports() {
        List<Product> products = MarketplaceData.getInstance().getAllProducts();
        List<User> users = MarketplaceData.getInstance().getAllUsers();
        List<Transaction> transactions = MarketplaceData.getInstance().getAllTransactions();

        ObservableList<Product> observableProducts = FXCollections.observableArrayList(products);
        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        ObservableList<Transaction> observableTransactions = FXCollections.observableArrayList(transactions);

        allProductsList.setItems(observableProducts);
        allUsersList.setItems(observableUsers);
        allTransactionsList.setItems(observableTransactions);

        // Calculate total revenue from all transactions
        double totalRevenue = 0;
        for (Transaction transaction : transactions) {
            totalRevenue += transaction.getProduct().getPrice();
        }
        totalRevenueLabel.setText("Total Revenue: $" + totalRevenue);
    }
}