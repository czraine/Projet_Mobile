package com.example.agencedevoyage.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.agencedevoyage.Entity.Offer;

import java.util.List;

@Dao
public interface OfferDao {

    @Insert
    void insertOffer(Offer offer);

    @Update
    void updateOffer(Offer offer);

    @Delete
    void deleteOffer(Offer offer);
    @Query("SELECT * FROM offers")
    List<Offer> getAllOffers();


    @Query("SELECT * FROM offers WHERE " +
            "(:destination IS NULL OR destination LIKE '%' || :destination || '%') " +
            "AND (:maxPrice = 0 OR price <= :maxPrice) " +
            "AND (:type IS NULL OR type = :type) " +
            "AND (:availabilityDate = 0 OR (availabilityStartDate <= :availabilityDate AND availabilityEndDate >= :availabilityDate)) " +
            "AND (:searchQuery IS NULL OR title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%' OR type LIKE '%' || :searchQuery || '%')")
    List<Offer> filterOffers(String destination, double maxPrice, String type, long availabilityDate, String searchQuery);
}
