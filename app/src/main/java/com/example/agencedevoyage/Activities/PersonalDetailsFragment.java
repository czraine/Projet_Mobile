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
 * Use the {@link PersonalDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PersonalDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalDetailsFragment newInstance(String param1, String param2) {
        PersonalDetailsFragment fragment = new PersonalDetailsFragment();
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

        private Button nextButton;
        private EditText etName, etUsername, etPassword;
        private UserViewModel userViewModel;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_personal_details, container, false);

            nextButton = view.findViewById(R.id.btnNext);
            etName = view.findViewById(R.id.etName);
            etUsername = view.findViewById(R.id.etUsername);
            etPassword = view.findViewById(R.id.etPassword);

            // Get the shared UserViewModel instance
            userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

            nextButton.setOnClickListener(v -> {
                // Validate input before proceeding
                if (validateInputs()) {
                    // Save input data to ViewModel
                    userViewModel.setName(etName.getText().toString());
                    userViewModel.setUsername(etUsername.getText().toString());
                    userViewModel.setPassword(etPassword.getText().toString());

                    // Move to the next fragment
                    ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            });



            return view;
        }
    // Validation method to ensure all inputs are valid
    private boolean validateInputs() {
        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Check if Name is empty
        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return false;
        }

        // Check if Username is empty or too short
        if (username.isEmpty()) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return false;
        } else if (username.length() < 4) {
            etUsername.setError("Username must be at least 4 characters");
            etUsername.requestFocus();
            return false;
        }

        // Check if Password is empty or too weak
        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        } else if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return false;
        }

        // All validations passed
        return true;
    }



}