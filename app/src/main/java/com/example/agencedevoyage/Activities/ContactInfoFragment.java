package com.example.agencedevoyage.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.agencedevoyage.Entity.UserViewModel;
import com.example.agencedevoyage.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactInfoFragment newInstance(String param1, String param2) {
        ContactInfoFragment fragment = new ContactInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
        private Button nextButton, previousButton;
        private EditText etEmail, etPhone, etStreet, etCity, etState, etCountry;
        private UserViewModel userViewModel;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_contact_info, container, false);

            nextButton = view.findViewById(R.id.btnNext);
            previousButton = view.findViewById(R.id.btnPrevious);
            etEmail = view.findViewById(R.id.etEmail);
            etPhone = view.findViewById(R.id.etPhone);
            etStreet = view.findViewById(R.id.etStreet);
            etCity = view.findViewById(R.id.etCity);
            etState = view.findViewById(R.id.etState);
            etCountry = view.findViewById(R.id.etCountry);

            // Get the shared UserViewModel instance
            userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

            // Navigate to the next fragment when "Next" button is clicked
            nextButton.setOnClickListener(v -> {
                if (validateInputs()) {
                    // Save contact info to ViewModel
                    userViewModel.setEmail(etEmail.getText().toString());
                    userViewModel.setPhone(etPhone.getText().toString());
                    userViewModel.setStreetAddress(etStreet.getText().toString());
                    userViewModel.setCity(etCity.getText().toString());
                    userViewModel.setState(etState.getText().toString());
                    userViewModel.setCountry(etCountry.getText().toString());

                    // Move to the next fragment
                    ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            });



            // Navigate to the previous fragment when "Previous" button is clicked
            previousButton.setOnClickListener(v -> {
                ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            });

            return view;
        }
    // Validation method to ensure inputs are valid
    private boolean validateInputs() {
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String street = etStreet.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String country = etCountry.getText().toString().trim();

        // Check if Email is valid
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return false;
        }

        // Check if Phone is valid (at least 8 digits)
        if (phone.isEmpty() || phone.length() < 8 || !phone.matches("\\d+")) {
            etPhone.setError("Enter a valid phone number");
            etPhone.requestFocus();
            return false;
        }

        // Check if Street address is empty
        if (street.isEmpty()) {
            etStreet.setError("Street address is required");
            etStreet.requestFocus();
            return false;
        }

        // Check if City is empty
        if (city.isEmpty()) {
            etCity.setError("City is required");
            etCity.requestFocus();
            return false;
        }

        // Check if State is empty
        if (state.isEmpty()) {
            etState.setError("State is required");
            etState.requestFocus();
            return false;
        }

        // Check if Country is empty
        if (country.isEmpty()) {
            etCountry.setError("Country is required");
            etCountry.requestFocus();
            return false;
        }

        // All validations passed
        return true;
    }

}
