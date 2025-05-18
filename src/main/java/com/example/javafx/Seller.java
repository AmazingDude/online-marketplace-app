
// Seller.java
package com.example.javafx;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Seller extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Product> productsForSale;

    public Seller(int userId, String username, String password, String email) {
        super(userId, username, password, email);
        this.productsForSale = new ArrayList<>();
    }

    public void addProduct(Product product) {
        productsForSale.add(product);
        System.out.println(getUsername() + " added " + product.getName());
    }

    public void removeProduct(Product product) {
        productsForSale.remove(product);
        System.out.println(getUsername() + " removed " + product.getName());
    }

    public List<Product> getProductsForSale() {
        return productsForSale;
    }

    @Override
    public void displayDashboard() {
        System.out.println("Seller Dashboard: Manage listed products.");
        // In a real application, this would show the seller's specific dashboard
    }
}