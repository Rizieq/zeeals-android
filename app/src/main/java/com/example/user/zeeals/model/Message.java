package com.example.user.zeeals.model;

public class Message {
    private String email;
    private String updated_at;
    private String created_at;
    private int id;
    private String url;
    private String message;

    public Message(String email, String updated_at, String created_at, int id, String url) {
        this.email = email;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.id = id;
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
