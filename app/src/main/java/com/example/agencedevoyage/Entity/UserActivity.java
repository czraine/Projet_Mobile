package com.example.agencedevoyage.Entity;

// UserActivity.java
import java.util.Date;
import com.google.gson.annotations.SerializedName;

public class UserActivity {
    @SerializedName("user_id")
    private int userId;

    @SerializedName("action")
    private String action;

    @SerializedName("timestamp")
    private String timestamp;

    public UserActivity(int userId, String action, String timestamp) {
        this.userId = userId;
        this.action = action;
        this.timestamp = timestamp;
    }

    // Getters and setters omitted for brevity
}

