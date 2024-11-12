package com.example.agencedevoyage.Domains;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "complaint")
public class Complaint {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String status;
    private String description;  // Nouvelle propriété

    // Constructor
    public Complaint(String title, String description, String status) {
        this.title = title;
        this.description = description;  // Assigne la description
        this.status = status;
    }

    // Getter et Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
