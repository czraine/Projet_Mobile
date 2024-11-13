package com.example.agencedevoyage.Services;

import com.stripe.model.Product;
import com.stripe.model.Price;
import com.stripe.param.ProductListParams;
import com.stripe.param.PriceListParams;

import java.util.List;

public class StripeService {

    // Initialize Stripe with API Key
    public static void initializeStripe(String apiKey) {
        com.stripe.Stripe.apiKey = "sk_test_51QAgm4KP2IKvZV2SjUq2UHWTCvLLazaRgWDShpxVY6VVrj9o3cMAv3lOlrL525dgmU5SLHC4gMayXzUUUs6KZgFT00gn2E3U6H";
    }

    // Fetch all products from Stripe
    public static List<Product> fetchProducts() throws Exception {
        ProductListParams params = ProductListParams.builder()
                .setActive(true)
                .build();
        return Product.list(params).getData();
    }

    // Fetch prices for a given product
    public static List<Price> fetchPrices(String productId) throws Exception {
        PriceListParams priceParams = PriceListParams.builder()
                .setProduct(productId)
                .setActive(true)
                .build();
        return Price.list(priceParams).getData();
    }
}

