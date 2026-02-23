package com.example.autofferandroid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.autofferandroid.databinding.FragmentSelectUserTypeBinding;
import com.example.core_models_sdk.models.UserType;

public class SelectUserTypeFragment extends Fragment {

    private FragmentSelectUserTypeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSelectUserTypeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cardPrivateCustomer.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(
                    SelectUserTypeFragmentDirections
                            .actionSelectUserTypeFragmentToRegisterFragment(UserType.PRIVATE_CUSTOMER)
            );
        });

        binding.cardArchitect.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(
                    SelectUserTypeFragmentDirections
                            .actionSelectUserTypeFragmentToRegisterFragment(UserType.ARCHITECT)
            );
        });

        binding.cardFactory.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(
                    SelectUserTypeFragmentDirections
                            .actionSelectUserTypeFragmentToRegisterFragment(UserType.FACTORY)
            );
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
