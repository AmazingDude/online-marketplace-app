// MarketplaceData.java
package com.example.javafx;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MarketplaceData implements Serializable {
    private static final long serialVersionUID = 1L;
    private static List<Product> products = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static List<Transaction> transactions = new ArrayList<>();
    private static List<Message> messages = new ArrayList<>(); // Made static
    private static int nextProductId = 1;
    private static int nextUserId = 1;
    private static int nextTransactionId = 1;
    private static int nextMessageId = 1; // Made static
    private static final String DATA_FILE = "marketplace_data.dat";

    // Private constructor to prevent instantiation (Singleton pattern)
    private MarketplaceData() {}

    // Static instance of MarketplaceData
    private static final MarketplaceData instance = new MarketplaceData();

    // Public static method to get the instance
    public static MarketplaceData getInstance() {
        return instance;
    }

    // Load data from file
    public static void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                products = (List<Product>) ois.readObject();
                users = (List<User>) ois.readObject();
                transactions = (List<Transaction>) ois.readObject();
                messages = (List<Message>) ois.readObject(); // Load messages
                nextProductId = products.isEmpty() ? 1 : products.get(products.size() - 1).getProductId() + 1;
                nextUserId = users.isEmpty() ? 1 : users.get(users.size() - 1).getUserId() + 1;
                nextTransactionId = transactions.isEmpty() ? 1 : transactions.get(transactions.size() - 1).getTransactionId() + 1;
                nextMessageId = messages.isEmpty() ? 1: messages.get(messages.size()-1).getMessageId() + 1; //load message ID
                System.out.println("Data loaded successfully.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data: " + e.getMessage());
                // Consider logging the error
            }        } else {
            System.out.println("No existing data file found. Starting fresh.");
        }
    }

    // Save data to file
    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(products);
            oos.writeObject(users);
            oos.writeObject(transactions);
            oos.writeObject(messages); // Save messages
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            // Consider logging the error
        }
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public void addProduct(Product product) {
        product.setProductId(nextProductId++);
        products.add(product);
        saveData(); // Save after adding
    }

    public void removeProduct(int productId) {
        products.removeIf(product -> product.getProductId() == productId);
        saveData();
    }

    public List<Product> searchProducts(String query) {
        String lowerQuery = query.toLowerCase();
        List<Product> results = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(lowerQuery) ||
                    product.getDescription().toLowerCase().contains(lowerQuery) ||
                    (product.getSeller() != null && product.getSeller().getUsername().toLowerCase().contains(lowerQuery))) { //added null check
                results.add(product);
            }
        }
        return results;
    }

    public void addUser(User user) {
        user.setUserId(nextUserId++);
        users.add(user);
        saveData();
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // In a real application, you'd handle authentication more securely
    public User authenticateUser(String username, String password) {
        User user = findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setTransactionId(nextTransactionId++);
        transactions.add(transaction);
        saveData();
    }

    public List<User> getAllUsers() {
        return users;
    }

    public void addMessage(Message message) {
        message.setMessageId(nextMessageId++);
        messages.add(message);
        saveData();
    }

    // New addMessage for replies
    public void addReply(int senderId, int receiverId, int productId, String content, int parentId) {
        Message reply = new Message(0, senderId, receiverId, productId, content, parentId);
        addMessage(reply);
    }

    public List<Message> getMessagesForUser(int userId) {
        List<Message> userMessages = new ArrayList<>();
        for (Message message : messages) {
            if (message.getSenderId() == userId || message.getReceiverId() == userId) {
                userMessages.add(message);
            }
        }
        return userMessages;
    }
}