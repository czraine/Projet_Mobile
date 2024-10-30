package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.agencedevoyage.Database.AppDatabase;
import com.example.agencedevoyage.Entity.User;
import com.example.agencedevoyage.Entity.UserViewModel;
import com.example.agencedevoyage.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfirmationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmationFragment newInstance(String param1, String param2) {
        ConfirmationFragment fragment = new ConfirmationFragment();
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

        private TextView tvName, tvUsername, tvEmail, tvPhone, tvAddress;
        private Button confirmButton, previousButton;
        private UserViewModel userViewModel;
        private ImageView ivProfilePicture;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);

        // Initialize views
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);
        tvName = view.findViewById(R.id.tvName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvAddress = view.findViewById(R.id.tvAddress);
        confirmButton = view.findViewById(R.id.btnConfirm);
        previousButton = view.findViewById(R.id.btnPrevious);

        // Get UserViewModel instance
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // Display data from ViewModel
        tvName.setText(userViewModel.getName());
        tvUsername.setText(userViewModel.getUsername());
        tvEmail.setText(userViewModel.getEmail());
        tvPhone.setText(userViewModel.getPhone());
        tvAddress.setText(userViewModel.getStreetAddress() + ", " + userViewModel.getCity() + ", " +
                userViewModel.getState() + ", " + userViewModel.getCountry());

        // Load the profile picture using Glide
        String imageUri = userViewModel.getProfileImageUri();
        if (imageUri != null) {
            Glide.with(this).load(imageUri).into(ivProfilePicture);
        }

        // Confirm button logic
        confirmButton.setOnClickListener(v -> {
            User user = new User(userViewModel.getName(), userViewModel.getUsername(),
                    userViewModel.getPassword(), userViewModel.getEmail(),
                    userViewModel.getPhone(), userViewModel.getStreetAddress(),
                    userViewModel.getCity(), userViewModel.getState(), userViewModel.getCountry(),userViewModel.getProfileImageUri());

            new Thread(() -> AppDatabase.getInstance(getContext()).userDao().insert(user)).start();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        // Navigate to the previous fragment
        previousButton.setOnClickListener(v -> {
            ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        });

        return view;
    }

}

