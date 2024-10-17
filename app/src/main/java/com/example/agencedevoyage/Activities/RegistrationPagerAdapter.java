package com.example.agencedevoyage.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class RegistrationPagerAdapter extends FragmentStateAdapter {

    // Constructor
    public RegistrationPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the fragment corresponding to the current step
        switch (position) {
            case 0:
                return new PersonalDetailsFragment(); // Step 1: Personal Details
            case 1:
                return new ContactInfoFragment();     // Step 2: Contact Information
            case 2:
                return new TravelInfoFragment();      // Step 3: Travel Information
            case 3:
                return new ProfileImageFragment();    // Step 4: Profile Image Upload
            case 4:
                return new ConfirmationFragment();    // Step 5: Confirmation
            default:
                return new PersonalDetailsFragment(); // Default case (first step)
        }
    }

    @Override
    public int getItemCount() {
        // Total number of steps (fragments)
        return 5;
    }
}
