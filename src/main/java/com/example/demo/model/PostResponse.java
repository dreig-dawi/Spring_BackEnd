package com.example.demo.model;

import java.sql.Timestamp;

public class PostResponse {
    private int id;
    private String description;
    private Timestamp created_at;
    private String username;

    public PostResponse(int id, String description, Timestamp created_at, String username) {
        this.id = id;
        this.description = description;
        this.created_at = created_at;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"created_at\":\"" + created_at + "\"," +
                "\"username\":\"" + username + "\"" +
                "}";
    }
}
