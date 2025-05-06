package com.example.demo.model;

    import jakarta.persistence.*;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import org.hibernate.annotations.CreationTimestamp;

    import java.sql.Timestamp;

@Entity
    @Table(name = "post", schema = "public")
    public class Post {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private String id;

        @Column(name = "description")
        private String description;

        @CreationTimestamp
        @Column(name = "created_at", updatable = false)
        private Timestamp createdAt;

        @Transient
        @JsonProperty("userEmail")
        private String userEmail;

        @ManyToOne
        @JoinColumn(name = "username", referencedColumnName = "username")
        private User user;

        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Timestamp getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public String toString() {
            return "Post{" +
                    "id='" + id + '\'' +
                    ", description='" + description + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", userEmail='" + userEmail + '\'' +
                    ", user=" + user +
                    '}';
        }
    }