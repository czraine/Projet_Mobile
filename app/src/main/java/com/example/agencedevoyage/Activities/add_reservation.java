package com.example.agencedevoyage.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.agencedevoyage.Dao.ReservationDAO;
import com.example.agencedevoyage.Entity.Reservation;
import com.example.agencedevoyage.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import android.content.Intent;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.ApiResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class add_reservation extends AppCompatActivity {

    private static final String TAG = "AddReservationActivity";
    private EditText firstNameEditText, lastNameEditText, phoneEditText, passeport, requirementsEditText;
    private RadioGroup offerRadioGroup;
    private Button confirmPaymentButton;
    private Button reservationButton;
    private CardInputWidget cardInputWidget;
    private Stripe stripe;
    private String clientSecret;
    private ArrayList<String> offerIds = new ArrayList<>();
    private ReservationDAO reservationDAO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        PaymentConfiguration.init(this, "pk_test_51QAgm4KP2IKvZV2S4zNZPqBKUUXFTzzfDPdVFB4PlBKWGPDIyGTknS6zrhYh3wlidoMnsEhyKpWwgaxQm4mv3lIX00baqSVBsx");
        stripe = new Stripe(this, PaymentConfiguration.getInstance(this).getPublishableKey());

        firstNameEditText = findViewById(R.id.first_name);
        lastNameEditText = findViewById(R.id.last_name);
        phoneEditText = findViewById(R.id.phone);
        passeport = findViewById(R.id.passeport);
        requirementsEditText = findViewById(R.id.requirement);
        offerRadioGroup = findViewById(R.id.offer_radio_group);
        confirmPaymentButton = findViewById(R.id.confirm_payment_button);
        reservationButton = findViewById(R.id.reservation_list_button);
        cardInputWidget = findViewById(R.id.card_input_widget);

        // Initialize DAO
        reservationDAO = new ReservationDAO(this);
        reservationDAO.open();

        fetchOffers();

        confirmPaymentButton.setOnClickListener(v -> initiatePaymentAndConfirm());

        // Set up the button to view reservations
        reservationButton.setOnClickListener(v -> {
            Intent intent = new Intent(add_reservation.this, ReservationListActivity.class);
            startActivity(intent);
        });

    }

    private void fetchOffers() {
        String url = "http://192.168.1.110:3000/list-products";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (offerRadioGroup != null) {  // Ensure RadioGroup is not null
                            offerRadioGroup.removeAllViews(); // Clear existing options
                            offerIds.clear(); // Clear previous offers
                        } else {
                            Log.e(TAG, "fetchOffers: RadioGroup offerRadioGroup is null");
                            return;
                        }

                        // Separate layout to show selected offer details
                        LinearLayout selectedOfferLayout = findViewById(R.id.selected_offer_layout);
                        ImageView selectedOfferImage = findViewById(R.id.selected_offer_image);
                        TextView selectedOfferName = findViewById(R.id.selected_offer_name);
                        TextView selectedOfferPrice = findViewById(R.id.selected_offer_price);
                        TextView selectedOfferDescription = findViewById(R.id.selected_offer_description);

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject offer = response.getJSONObject(i);
                            String offerName = offer.getString("name");
                            int price = offer.getInt("price") / 100; // Convert cents to dollars
                            String description = offer.getString("description");
                            String imageUrl = offer.getString("image");
                            String offerId = offer.getString("id");

                            // Create a RadioButton for each offer
                            RadioButton radioButton = new RadioButton(this);
                            radioButton.setText(String.format("%s - $%.2f", offerName, (float) price));
                            radioButton.setTag(offerId); // Store offer ID as tag

                            // Add click listener to show offer details when selected
                            radioButton.setOnClickListener(view -> {
                                // Load the image asynchronously
                                Glide.with(this).load(imageUrl).into(selectedOfferImage);

                                // Set the text fields for name, price, and description
                                selectedOfferName.setText(offerName);
                                selectedOfferPrice.setText(String.format("$%.2f", (float) price));
                                selectedOfferDescription.setText(description);

                                // Make the selected offer layout visible
                                selectedOfferLayout.setVisibility(View.VISIBLE);
                            });

                            // Add RadioButton directly to RadioGroup for single selection handling
                            offerRadioGroup.addView(radioButton);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "fetchOffers: JSON Exception: " + e.getMessage());
                    }
                },
                error -> Log.e(TAG, "fetchOffers: Error fetching offers: " + error.getMessage())
        );

        queue.add(jsonArrayRequest);
    }

    private void initiatePaymentAndConfirm() {
        int selectedRadioButtonId = offerRadioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, "Please select an offer", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String productId = (String) selectedRadioButton.getTag();

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("productId", productId);
        } catch (JSONException e) {
            Log.e(TAG, "initiatePayment: JSON Exception: " + e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://192.168.1.110:3000/create-payment-intent", jsonBody,
                response -> {
                    try {
                        clientSecret = response.getString("clientSecret");
                        confirmPayment();
                    } catch (JSONException e) {
                        Log.e(TAG, "initiatePayment: JSON Exception: " + e.getMessage());
                    }
                },
                error -> Log.e(TAG, "initiatePayment: Error creating payment intent: " + error.getMessage())
        );

        queue.add(jsonObjectRequest);
    }


    private void confirmPayment() {
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
        if (params != null && clientSecret != null) {
            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(params, clientSecret);
            stripe.confirmPayment(this, confirmParams);
        } else {
            Toast.makeText(this, "Invalid payment details", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "confirmPayment: Invalid payment details.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        stripe.retrievePaymentIntent(clientSecret, new ApiResultCallback<PaymentIntent>() {
            @Override
            public void onSuccess(@NonNull PaymentIntent paymentIntent) {
                PaymentIntent.Status status = paymentIntent.getStatus();
                if (status == PaymentIntent.Status.Succeeded) {
                    Toast.makeText(add_reservation.this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                    saveReservationToDatabase();
                } else {
                    Toast.makeText(add_reservation.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull Exception e) {
                Toast.makeText(add_reservation.this, "Error retrieving payment: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveReservationToDatabase() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String selectedOffer = ((RadioButton) findViewById(offerRadioGroup.getCheckedRadioButtonId())).getText().toString();
        String passport = passeport.getText().toString().trim();  // Ensure "passport" instead of "passeport"
        String requirements = requirementsEditText.getText().toString().trim();

        Reservation reservation = new Reservation(firstName, lastName, phone, passport, requirements);
        long result = reservationDAO.insertReservation(reservation);
        if (result != -1) {
            Toast.makeText(this, "Reservation saved successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save reservation", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        reservationDAO.close();
    }
}


