package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "content", schema = "public")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "data")
    private String data;

    @Transient
    @JsonProperty("post_id")
    private int postIdInput;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post postId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPostIdInput() {
        return postIdInput;
    }

    public void setPostIdInput(int postIdInput) {
        this.postIdInput = postIdInput;
    }

    public Post getPostId() {
        return postId;
    }

    public void setPostId(Post postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"data\":\"" + data + "\"," +
                "\"postId\":" + (postId != null ? "\"" + postId.getId() + "\"" : "null") + "," +
                "\"postIdInput\":" + postIdInput +
                "}";
    }
}
