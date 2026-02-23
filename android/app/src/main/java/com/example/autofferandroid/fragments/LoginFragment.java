package com.example.autofferandroid.fragments;

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
import com.example.autofferandroid.databinding.FragmentLoginBinding;
import com.example.users_sdk.network.SessionManager;
import com.example.users_sdk.network.UserManager;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private final UserManager userManager = new UserManager();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonLogin.setOnClickListener(v -> attemptLogin());

        binding.registerRedirectText.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_selectUserTypeFragment));
    }

    private void attemptLogin() {
        String email = binding.inputEmail.getText() != null ? binding.inputEmail.getText().toString().trim() : "";
        String password = binding.inputPassword.getText() != null ? binding.inputPassword.getText().toString().trim() : "";
        if (TextUtils.isEmpty(email)) {
            binding.emailLayout.setError("Email is required");
            return;
        } else {
            binding.emailLayout.setError(null);
        }
        if (TextUtils.isEmpty(password)) {
            binding.passwordLayout.setError("Password is required");
            return;
        } else {
            binding.passwordLayout.setError(null);
        }
        binding.buttonLogin.setEnabled(false);
        userManager.loginUser(email, password).thenAccept(user -> {
            if (getActivity() == null) return;
            SessionManager.getInstance().setCurrentUser(user);
                requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_homeFragment);
            });

        }).exceptionally(throwable -> {
            if (getActivity() == null) return null;
                requireActivity().runOnUiThread(() -> {
                binding.buttonLogin.setEnabled(true);
                Toast.makeText(requireContext(), "Login failed: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
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
