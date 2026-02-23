package com.example.autofferandroid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.autofferandroid.R;
import com.example.autofferandroid.databinding.FragmentResetPasswordBinding;
import com.example.users_sdk.network.UserManager;
import com.example.users_sdk.requests.ResetPasswordRequest;

public class ResetPasswordFragment extends Fragment {

    private FragmentResetPasswordBinding binding;
    private final UserManager userManager = new UserManager();
    private String verifiedPhoneNumber;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            verifiedPhoneNumber = getArguments().getString("phoneNumber");
        }

        binding.buttonSubmitNewPassword.setOnClickListener(v -> submitNewPassword());
    }

    private void submitNewPassword() {
        String newPassword = binding.inputNewPassword.getText() != null ?
                binding.inputNewPassword.getText().toString().trim() : "";
        String confirmPassword = binding.inputConfirmPassword.getText() != null ?
                binding.inputConfirmPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(requireContext(), "Please enter both password fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 8) {
            binding.inputNewPassword.setError("Password must be at least 8 characters");
            return;
        } else {
            binding.inputNewPassword.setError(null);
        }

        if (!newPassword.equals(confirmPassword)) {
            binding.inputConfirmPassword.setError("Passwords do not match");
            return;
        } else {
            binding.inputConfirmPassword.setError(null);
        }

        if (verifiedPhoneNumber == null || verifiedPhoneNumber.isEmpty()) {
            Toast.makeText(requireContext(), "Missing verified phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        ResetPasswordRequest request = new ResetPasswordRequest(verifiedPhoneNumber, newPassword);

        binding.buttonSubmitNewPassword.setEnabled(false);

        userManager.resetPassword(request)
                .thenRun(() -> requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Password reset successfully", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_resetPasswordFragment_to_loginFragment);
                }))
                .exceptionally(e -> {
                    requireActivity().runOnUiThread(() -> {
                        binding.buttonSubmitNewPassword.setEnabled(true);
                        Toast.makeText(requireContext(), "Failed to reset password: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
