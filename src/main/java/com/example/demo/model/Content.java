package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "content", schema = "public")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "data")
    private String data;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "post_id")
    private Post postId;
}
