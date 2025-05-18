package com.example.javafx;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private int messageId;
    private int senderId;
    private int receiverId;
    private int productId;
    private String content;
    private int parentId; // Add this field

    public Message(int messageId, int senderId, int receiverId, int productId, String content) {
        this(messageId, senderId, receiverId, productId, content, 0); // Default parentId to 0 (no parent)
    }

    public Message(int messageId, int senderId, int receiverId, int productId, String content, int parentId) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.productId = productId;
        this.content = content;
        this.parentId = parentId;
    }

    // Getters and setters for all fields, including parentId

    public int getMessageId() {
        return messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public int getProductId() {
        return productId;
    }

    public String getContent() {
        return content;
    }

    public int getParentId() {
        return parentId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }


//    @Override
//    public String toString() {
//        return "Message{" +
//                "messageId=" + messageId +
//                ", senderId=" + senderId +
//                ", receiverId=" + receiverId +
//                ", productId=" + productId +
//                ", content='" + content + '\'' +
//                ", timestamp=" + timestamp +
//                '}';
//    }
}
