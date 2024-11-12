package com.example.agencedevoyage.Domains;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private Integer id;
    private String description;
    private String date;
    private String imageUrl;
    private static final String USERNAME = "Oussema Mahjoubi";




    // Liste des commentaires associés au post
    private List<String> comments;

    // Constructeur par défaut
    public Post() {
        this.comments = new ArrayList<>();
    }

    // Constructeur avec paramètres
    public Post(Integer id, String description, String date, String imageUrl) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.imageUrl = imageUrl;
        this.comments = new ArrayList<>();
    }



    // Getters et setters
    public Integer getId() { return id; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getImageUrl() { return imageUrl; }
    public List<String> getComments() { return comments; }

    public void setId(Integer id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // Ajoute un commentaire
    public void addComment(String comment) { comments.add(comment); }
    public static String getUSERNAME() {
        return USERNAME;
    }
    public void setComments(List<String> comments) {
        this.comments = comments;
    }
    // toString
    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", comments=" + comments +
                '}';
    }
}
