package com.gelora.absensi.model;

public class ChatModel {

    private String id;
    private String message;
    private String sender;
    private String receiver;
    private String timestamp;
    private String first_chat;
    private String read_status;

    public String getId() {
        return id;
    }

    public void setId(String idChat) {
        this.id = idChat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String reciever) {
        this.receiver = reciever;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFirst_chat() {
        return first_chat;
    }

    public void setFirst_chat(String first_chat) {
        this.first_chat = first_chat;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }
}
