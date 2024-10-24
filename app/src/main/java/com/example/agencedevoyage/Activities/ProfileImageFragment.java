package com.example.agencedevoyage.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.agencedevoyage.Entity.UserViewModel;
import com.example.agencedevoyage.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileImageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileImageFragment newInstance(String param1, String param2) {
        ProfileImageFragment fragment = new ProfileImageFragment();
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
        private UserViewModel userViewModel;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile_image, container, false);

            nextButton = view.findViewById(R.id.btnNext);
            previousButton = view.findViewById(R.id.btnPrevious);

            // Get the shared UserViewModel instance
            userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

            // Assuming profile picture upload logic
            Button uploadImageButton = view.findViewById(R.id.btnUploadImage);
            uploadImageButton.setOnClickListener(v -> {
                // Logic to select and upload the image (this is just a placeholder)
                // Assuming we get an image URI or file path after the upload
                String imagePath = "path/to/uploaded/image";  // Replace with actual image path
                userViewModel.setProfilePicture(imagePath);
            });

            // Navigate to the next fragment when "Next" button is clicked
            nextButton.setOnClickListener(v -> {
                ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            });

            // Navigate to the previous fragment when "Previous" button is clicked
            previousButton.setOnClickListener(v -> {
                ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            });

            return view;
        }
    }
