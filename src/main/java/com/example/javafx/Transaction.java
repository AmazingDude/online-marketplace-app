// Transaction.java
package com.example.javafx;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private int transactionId;
    private Buyer buyer;
    private Seller seller;
    private Product product;
    private LocalDateTime transactionDate;

    public Transaction(int transactionId, Buyer buyer, Seller seller, Product product) {
        this.transactionId = transactionId;
        this.buyer = buyer;
        this.seller = seller;
        this.product = product;
        this.transactionDate = LocalDateTime.now();
    }

    // Getters and setters
    public int getTransactionId() {
        return transactionId;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public Seller getSeller() {
        return seller;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionId(int transactionId){
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", buyer=" + buyer.getUsername() +
                ", seller=" + seller.getUsername() +
                ", product=" + product.getName() +
                ", transactionDate=" + transactionDate +
                '}';
    }
}