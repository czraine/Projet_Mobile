package com.example.agencedevoyage.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.agencedevoyage.Domains.Complaint;

import java.util.List;

@Dao
public interface ComplaintDAO {

    @Insert
    void insertComplaint(Complaint complaint);

    @Query("SELECT * FROM complaint")
    List<Complaint> getAllComplaints();

    @Query("UPDATE complaint SET status = :status WHERE title = :title")
    void updateComplaintStatus(String title, String status);

    @Query("UPDATE complaint SET description = :description WHERE title = :title")
    void updateComplaintDescription(String title, String description);

    @Query("SELECT * FROM complaint WHERE id = :complaintId")
    Complaint getComplaintById(int complaintId);

    @Query("UPDATE complaint SET title = :newTitle, description = :newDescription WHERE id = :complaintId")
    void updateComplaint(int complaintId, String newTitle, String newDescription);


    @Query("DELETE FROM complaint WHERE id = :complaintId")
    void deleteComplaintById(int complaintId);

    @Delete
    void deleteComplaint(Complaint complaint);
}
