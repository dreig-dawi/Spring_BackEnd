package com.example.demo.model;

public class ContentResponse {
    private int id;
    private int post_id;
    private String data;

    public ContentResponse(int id, int post_id, String data) {
        this.id = id;
        this.post_id = post_id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"post_id\":\"" + post_id + "\"," +
                "\"data\":\"" + data + "\"" +
                "}";
    }
}
