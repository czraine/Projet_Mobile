package com.example.agencedevoyage.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.agencedevoyage.Domains.Complaint;

import java.util.List;

@Dao
public interface ComplaintDAO {

    // Insert a new complaint
    @Insert
    void insertComplaint(Complaint complaint);

    // Retrieve all complaints
    @Query("SELECT * FROM complaint")
    List<Complaint> getAllComplaints();

    // Retrieve a specific complaint by its ID
    @Query("SELECT * FROM complaint WHERE id = :complaintId")
    Complaint getComplaintById(int complaintId);

    // Retrieve complaints by status
    @Query("SELECT * FROM complaint WHERE status = :status")
    List<Complaint> getComplaintsByStatus(String status);

    // Update the status of a specific complaint
    @Query("UPDATE complaint SET status = :status WHERE id = :complaintId")
    void updateComplaintStatus(int complaintId, String status);

    // Update the description of a specific complaint
    @Query("UPDATE complaint SET description = :description WHERE id = :complaintId")
    void updateComplaintDescription(int complaintId, String description);

    // Update both title and description for a specific complaint
    @Query("UPDATE complaint SET title = :newTitle, description = :newDescription WHERE id = :complaintId")
    void updateComplaint(int complaintId, String newTitle, String newDescription);

    // Update rating and feedback for a resolved complaint
    @Query("UPDATE complaint SET rating = :rating, feedback = :feedback WHERE id = :complaintId")
    void updateComplaintFeedback(int complaintId, int rating, String feedback);


    // Delete a complaint by its ID
    @Query("DELETE FROM complaint WHERE id = :complaintId")
    void deleteComplaintById(int complaintId);

    // Delete a specific complaint by passing the complaint object
    @Delete
    void deleteComplaint(Complaint complaint);
}
