
// Product.java
package com.example.javafx;

import javafx.fxml.Initializable;

import java.io.Serializable;

public class Product implements Serializable {
    int productId;
    private String name;
    private String description;
    private double price;
    private Seller seller;

    public Product(int productId, String name, String description, double price, Seller seller) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.seller = seller;
    }

    // Getters and setters
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public Seller getSeller() { return seller; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setSeller(Seller seller) { this.seller = seller; }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", seller=" + seller.getUsername() +
                '}';
    }
    public void setProductId(int productId){
        this.productId = productId;
    }
}