package com.example.agencedevoyage.Database;

import java.util.ArrayList;
import java.util.List;

public class OffreList {
    public static List<String> getOffers() {
        List<String> offers = new ArrayList<>();
        offers.add("Special Vacation Package - $499.99");
        offers.add("Luxury Cruise - $799.99");
        offers.add("Adventure Tour - $399.99");
        return offers;
    }
}
