package com.example.autofferandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.core_models_sdk.models.UserType;
import com.example.users_sdk.network.UserManager;
import com.example.users_sdk.requests.RegisterUserRequest;

import com.example.autofferandroid.databinding.ActivityRegisterBinding;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userManager = new UserManager();

        binding.buttonRegister.setOnClickListener(v -> registerUser());

        binding.loginRedirectText.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String firstName = Objects.requireNonNull(binding.inputFirstName.getText()).toString().trim();
        String lastName = Objects.requireNonNull(binding.inputLastName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.inputEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.inputPassword.getText()).toString();
        String confirmPassword = Objects.requireNonNull(binding.inputConfirmPassword.getText()).toString();
        String phone = Objects.requireNonNull(binding.inputPhone.getText()).toString().trim();
        String address = Objects.requireNonNull(binding.inputAddress.getText()).toString().trim();

        UserType userType = binding.profileToggleGroup.getCheckedButtonId() == binding.profileArchitect.getId()
                ? UserType.ARCHITECT
                : UserType.PRIVATE_CUSTOMER;

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
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
                .thenAccept(registeredUser -> runOnUiThread(() -> {
                    binding.buttonRegister.setEnabled(true);
                    if (registeredUser != null) {
                        Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                }))
                .exceptionally(e -> {
                    runOnUiThread(() -> {
                        binding.buttonRegister.setEnabled(true);
                        Toast.makeText(this, "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                    return null;
                });
    }
}
