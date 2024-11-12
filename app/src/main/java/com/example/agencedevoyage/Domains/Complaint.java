package com.example.agencedevoyage.Domains;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "complaint")
public class Complaint {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String status;
    private String description;

    // New fields for rating and feedback
    private int rating; // A value from 1 to 5
    private String feedback; // User's feedback comment

    // Constructor
    public Complaint(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.rating = -1; // Default value indicating no rating
        this.feedback = null; // Default value indicating no feedback
    }

    // Getters and Setters for existing fields
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // New getters and setters for rating and feedback
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
