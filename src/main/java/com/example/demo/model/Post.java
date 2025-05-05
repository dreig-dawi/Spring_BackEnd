package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "post", schema = "public")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private String createdAt;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "username")
    private User userEmail;
}
