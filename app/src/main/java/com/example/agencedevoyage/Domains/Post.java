package com.example.agencedevoyage.Domains;




public class Post {
    private String userName;
    private String description;
    private String date;

    public Post(String userName, String description, String date) {
        this.userName = userName;
        this.description = description;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
