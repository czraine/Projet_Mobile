package com.example.agencedevoyage.Dao;


import com.example.agencedevoyage.Domains.Complaint;

public interface OnComplaintSelectedListener {
    void onComplaintSelected(Complaint complaint);
    void onEditComplaint(Complaint complaint);
    void onDeleteComplaint(Complaint complaint);
}