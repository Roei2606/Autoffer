package com.example.autofferandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.autofferandroid.databinding.ActivityResetPasswordBinding;
import com.example.users_sdk.network.UserManager;
import com.example.users_sdk.requests.ResetPasswordRequest;


import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {

    private ActivityResetPasswordBinding binding;
    private UserManager userManager;
    private String verifiedPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userManager = new UserManager();
        verifiedPhoneNumber = getIntent().getStringExtra("phoneNumber");

        binding.buttonSubmitNewPassword.setOnClickListener(v -> submitNewPassword());
    }

    private void submitNewPassword() {
        String newPassword = Objects.requireNonNull(binding.inputNewPassword.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(binding.inputConfirmPassword.getText()).toString().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please enter both password fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 8) {
            binding.inputNewPassword.setError("Password must be at least 8 characters");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            binding.inputConfirmPassword.setError("Passwords do not match");
            return;
        }

        ResetPasswordRequest request = new ResetPasswordRequest(verifiedPhoneNumber, newPassword);

        binding.buttonSubmitNewPassword.setEnabled(false);

        userManager.resetPassword(request)
                .thenRun(() -> runOnUiThread(() -> {
                    Toast.makeText(this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }))
                .exceptionally(e -> {
                    runOnUiThread(() -> {
                        binding.buttonSubmitNewPassword.setEnabled(true);
                        Toast.makeText(this, "Failed to reset password: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                    return null;
                });
    }
}
