// Buyer.java
package com.example.javafx;

import java.io.Serializable;

public class Buyer extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    public Buyer(int userId, String username, String password, String email) {
        super(userId, username, password, email);
    }

    public void buyProduct(Product product) {
        System.out.println(getUsername() + " bought " + product.getName());
        // In a real application, this would involve creating a Transaction
    }

    @Override
    public void displayDashboard() {
        System.out.println("Buyer Dashboard: View available products.");
        // In a real application, this would show the buyer's specific dashboard
    }
}
