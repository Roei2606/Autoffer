package com.example.autofferandroid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.autofferandroid.adapters.UserCardAdapter;
import com.example.autofferandroid.databinding.FragmentUserDirectoryBinding;
import com.example.core_models_sdk.models.User;
import com.example.core_models_sdk.models.UserType;
import com.example.users_sdk.network.SessionManager;
import com.example.users_sdk.network.UserManager;


import java.util.ArrayList;
import java.util.List;

public class UserDirectoryFragment extends Fragment {

    private FragmentUserDirectoryBinding binding;
    private UserCardAdapter adapter;
    private UserManager userManager;
    private UserType currentUserType;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserDirectoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userManager = new UserManager();
        adapter = new UserCardAdapter(new ArrayList<>(), requireContext());
        binding.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewUsers.setAdapter(adapter);

        currentUserType = SessionManager.getInstance().getCurrentUser().getProfileType();

        if (currentUserType == UserType.PRIVATE_CUSTOMER) {
            setupToggleBar();
            fetchArchitects();
        } else {
            binding.profileTypeToggleGroup.setVisibility(View.GONE);
            if (currentUserType == UserType.ARCHITECT) {
                fetchFactories();
            } else if (currentUserType == UserType.FACTORY) {
                fetchRelevantCustomersForFactory();
            }
        }
    }

    private void setupToggleBar() {
        binding.profileTypeToggleGroup.setVisibility(View.VISIBLE);
        binding.profileTypeToggleGroup.check(binding.buttonArchitects.getId());

        binding.profileTypeToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == binding.buttonArchitects.getId()) {
                    fetchArchitects();
                } else if (checkedId == binding.buttonFactories.getId()) {
                    fetchFactories();
                }
            }
        });
    }

    private void fetchArchitects() {
        userManager.getUsersByType(UserType.ARCHITECT).thenAccept(this::updateUserList)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    private void fetchFactories() {
        userManager.getUsersByType(UserType.FACTORY).thenAccept(this::updateUserList)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    private void fetchRelevantCustomersForFactory() {
        // ימומש בשלב הבא לפי חוקיות הפרויקטים המשותפים
    }

    private void updateUserList(List<User> users) {
        requireActivity().runOnUiThread(() -> adapter.updateData(users));
    }
}
