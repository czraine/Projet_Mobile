package com.example.agencedevoyage.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "offers")
public class Offer implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String destination;
    private double price;
    private String imageUri;
    private String type;  // New field for category/type
    private long availabilityStartDate;  // Unix timestamp for start date
    private long availabilityEndDate;    // Unix timestamp for end date
    private float totalRatingPoints;  // Total sum of ratings
    private int reviewCount;          // Number of ratings
    private String reviews = "";      // Store multiple reviews as text

    // Constructors, including the new fields
    public Offer(String title, String description, String destination, double price, String imageUri, String type, long availabilityStartDate, long availabilityEndDate) {
        this.title = title;
        this.description = description;
        this.destination = destination;
        this.price = price;
        this.imageUri = imageUri;
        this.type = type;
        this.availabilityStartDate = availabilityStartDate;
        this.availabilityEndDate = availabilityEndDate;
        this.totalRatingPoints = 0;
        this.reviewCount = 0;
    }

    // Getters and setters for all fields
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public long getAvailabilityStartDate() { return availabilityStartDate; }
    public void setAvailabilityStartDate(long availabilityStartDate) { this.availabilityStartDate = availabilityStartDate; }

    public long getAvailabilityEndDate() { return availabilityEndDate; }
    public void setAvailabilityEndDate(long availabilityEndDate) { this.availabilityEndDate = availabilityEndDate; }

    // Getters and setters for totalRatingPoints and reviewCount
    public float getTotalRatingPoints() { return totalRatingPoints; }
    public void setTotalRatingPoints(float totalRatingPoints) { this.totalRatingPoints = totalRatingPoints; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    // Calculated average rating based on total points and review count
    public float getAverageRating() {
        return reviewCount == 0 ? 0 : totalRatingPoints / reviewCount;
    }

    public void addRating(float rating) {
        this.totalRatingPoints += rating;
        this.reviewCount++;
    }

    // Getters and setters for reviews
    public String getReviews() { return reviews; }
    public void setReviews(String reviews) { this.reviews = reviews; }

    public void addReview(String review) {
        if (this.reviews.isEmpty()) {
            this.reviews = review;
        } else {
            this.reviews += "\n\n" + review;  // Append with a double newline separator
        }
    }
}
