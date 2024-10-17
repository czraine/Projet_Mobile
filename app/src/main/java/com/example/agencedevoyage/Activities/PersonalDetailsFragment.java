package com.example.agencedevoyage.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_details, container, false);

        nextButton = view.findViewById(R.id.btnNext);

        // Navigate to next step (fragment) when "Next" button is clicked
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Accessing ViewPager2 from activity
                ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1); // Move to the next fragment
            }
        });

        return view;
    }
}