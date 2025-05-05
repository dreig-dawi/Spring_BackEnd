package com.example.demo.model;

import jakarta.persistence.*;

@Entity(name = "teste")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessages(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }}
