package com.example.autofferandroid.fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.autofferandroid.R;

import com.example.autofferandroid.databinding.FragmentRegisterBinding;
import com.example.core_models_sdk.models.UserType;
import com.example.users_sdk.network.UserManager;
import com.example.users_sdk.requests.RegisterUserRequest;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private final UserManager userManager = new UserManager();
    private UserType userType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            userType = RegisterFragmentArgs.fromBundle(getArguments()).getUserType();
        }

        binding.buttonRegister.setOnClickListener(v -> registerUser());

        binding.loginRedirectText.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment));
    }

    private void registerUser() {
        String firstName = Objects.requireNonNull(binding.inputFirstName.getText()).toString().trim();
        String lastName = Objects.requireNonNull(binding.inputLastName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.inputEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.inputPassword.getText()).toString();
        String confirmPassword = Objects.requireNonNull(binding.inputConfirmPassword.getText()).toString();
        String phone = Objects.requireNonNull(binding.inputPhone.getText()).toString().trim();
        String address = Objects.requireNonNull(binding.inputAddress.getText()).toString().trim();

        if (userType == null) {
            Toast.makeText(requireContext(), "User type is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.inputEmail.setError("Invalid email format");
            return;
        }

        if (password.length() < 8) {
            binding.inputPassword.setError("Password must be at least 8 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            binding.inputConfirmPassword.setError("Passwords do not match");
            return;
        }

        RegisterUserRequest request = new RegisterUserRequest(
                firstName,
                lastName,
                email,
                password,
                phone,
                address,
                userType
        );

        binding.buttonRegister.setEnabled(false);

        userManager.registerUser(request)
                .thenAccept(user -> {
                    if (getActivity() == null) return;
                        requireActivity().runOnUiThread(() -> {
                        binding.buttonRegister.setEnabled(true);
                        Toast.makeText(requireContext(), "Registered successfully!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(binding.getRoot())
                                .navigate(R.id.action_registerFragment_to_loginFragment);
                    });
                })
                .exceptionally(e -> {
                    if (getActivity() == null) return null;
                    requireActivity().runOnUiThread(() -> {
                        binding.buttonRegister.setEnabled(true);
                        Toast.makeText(requireContext(), "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                    return null;
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
