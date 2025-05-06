package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user", schema = "public")
public class User {
    @Column(name = "username")
    private String username;

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String toString() {
        return "{" +
                "\"email\":\"" + email + "\"," +
                "\"username\":\"" + username + "\"," +
                "\"passwordHash\":\"" + passwordHash + "\"" +
                "}";
    }
}
