// InboxController.java
package com.example.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InboxController {

    @FXML
    private TabPane conversationTabs;
    @FXML
    private TextArea replyTextArea;
    @FXML
    private Button sendReplyButton;

    private int loggedInUserId;
    private Integer currentInterlocutorId = null;

    private Map<Integer, ObservableList<String>> conversations = new HashMap<>();
    private Map<Integer, Message> latestMessageForContact = new HashMap<>();

    public void initialize() {
        conversationTabs.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null && newTab.getUserData() instanceof Integer) {
                currentInterlocutorId = (Integer) newTab.getUserData();
            } else {
                currentInterlocutorId = null;
            }
        });
    }

    public void loadMessages(int userId) {
        loggedInUserId = userId;
        Map<Integer, List<Message>> groupedMessages = new HashMap<>();
        List<Message> allMessages = MarketplaceData.getInstance().getMessagesForUser(userId);

        for (Message message : allMessages) {
            int otherUserId = (message.getSenderId() == userId) ? message.getReceiverId() : message.getSenderId();
            groupedMessages.computeIfAbsent(otherUserId, k -> new ArrayList<>()).add(message);
            latestMessageForContact.put(otherUserId, message);
        }

        for (Map.Entry<Integer, List<Message>> entry : groupedMessages.entrySet()) {
            int contactId = entry.getKey();
            List<Message> messagesForContact = entry.getValue();

            User contact = MarketplaceData.getInstance().getAllUsers().stream()
                    .filter(user -> user.getUserId() == contactId)
                    .findFirst().orElse(null);
            String contactName = (contact != null) ? contact.getUsername() : "Unknown";

            Tab tab = conversationTabs.getTabs().stream()
                    .filter(t -> t.getUserData() != null && t.getUserData().equals(contactId))
                    .findFirst().orElse(null);

            ObservableList<String> observableMessages = conversations.computeIfAbsent(contactId, k -> FXCollections.observableArrayList());
            observableMessages.clear();
            for (Message msg : messagesForContact) {
                observableMessages.add(formatMessage(msg, userId, contactId)); // Pass contactId
            }

            if (tab == null) {
                tab = new Tab(contactName);
                ListView<String> messageList = new ListView<>(observableMessages);
                tab.setContent(messageList);
                tab.setUserData(contactId);
                conversationTabs.getTabs().add(tab);
            } else {
                ListView<String> messageList = (ListView<String>) tab.getContent();
                messageList.setItems(observableMessages);
                tab.setText(contactName);
            }
        }

        conversationTabs.getTabs().removeIf(tab -> {
            Integer contactId = (Integer) tab.getUserData();
            return contactId != null && !groupedMessages.containsKey(contactId);
        });
    }

    private String formatMessage(Message message, int loggedInUserId, int contactId) {
        User sender = MarketplaceData.getInstance().getAllUsers().stream()
                .filter(user -> user.getUserId() == message.getSenderId())
                .findFirst().orElse(null);
        String senderUsername = (sender != null) ? sender.getUsername() : "Unknown";

        String direction;
        if (message.getSenderId() == loggedInUserId) {
            direction = "You:";
        } else {
            User otherUser = MarketplaceData.getInstance().getAllUsers().stream()
                    .filter(user -> user.getUserId() == contactId)
                    .findFirst().orElse(null);
            String otherUsername = (otherUser != null) ? otherUser.getUsername() : "Unknown";
            direction = otherUsername + ":";
        }
        return direction + " " + message.getContent();
    }

    @FXML
    protected void sendReply() {
        if (currentInterlocutorId != null && !replyTextArea.getText().trim().isEmpty()) {
            Message latestMessage = latestMessageForContact.get(currentInterlocutorId);
            if (latestMessage != null) {
                int receiverId = currentInterlocutorId;
                int productId = latestMessage.getProductId();
                String content = replyTextArea.getText().trim();
                int parentId = latestMessage.getMessageId();

                MarketplaceData.getInstance().addReply(loggedInUserId, receiverId, productId, content, parentId);
                replyTextArea.clear();
                loadMessages(loggedInUserId);
            }
        } else {
            System.out.println("Please select a conversation and enter a reply.");
            // Optionally show an alert
        }
    }
}