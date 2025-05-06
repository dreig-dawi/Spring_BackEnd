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

        @Override
        public String toString() {
            return "Content{" +
                    "id='" + id + '\'' +
                    ", data='" + data + '\'' +
                    ", postId=" + postId +
                    '}';
        }
}
