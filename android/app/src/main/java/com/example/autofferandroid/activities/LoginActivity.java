package com.example.autofferandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import com.example.autofferandroid.databinding.ActivityLoginBinding;
import com.example.rsocket_sdk.network.RSocketClientManager;
import com.example.users_sdk.network.SessionManager;
import com.example.users_sdk.network.UserManager;

import java.util.Objects;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userManager = new UserManager();

        // 🟢 Connect to RSocket server in background
        new Thread(() -> {
            boolean connected = RSocketClientManager.getInstance().connect();
            runOnUiThread(() -> {
                if (connected) {
                    Toast.makeText(this, "Connected to server", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to connect to server", Toast.LENGTH_LONG).show();
                }
            });
        }).start();

        // 🟢 Login button
        binding.loginButton.setOnClickListener(v -> loginUser());

        // 🟢 Register button
        binding.registerText.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        // 🟢 Forgot password
        binding.forgotPasswordText.setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void loginUser() {
        String email = Objects.requireNonNull(binding.usernameInput.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.passwordInput.getText()).toString().trim();

        if (!isValidEmail(email)) {
            binding.usernameInput.setError("Invalid email format");
            return;
        }

        if (password.length() < 8) {
            binding.passwordInput.setError("Password must be at least 8 characters");
            return;
        }

        binding.loginButton.setEnabled(false);

        userManager.loginUser(email, password)
                .thenAccept(user -> runOnUiThread(() -> {
                    binding.loginButton.setEnabled(true);
                    if (user != null) {
                        SessionManager.getInstance().setCurrentUser(user); // ✅ לשמור את כל ה-User
                        Toast.makeText(this, "Welcome " + user.getFirstName(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }

                }))
                .exceptionally(e -> {
                    runOnUiThread(() -> {
                        binding.loginButton.setEnabled(true);
                        Toast.makeText(this, "Login error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                    return null;
                });
    }

    private boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        return emailPattern.matcher(email).matches();
    }
}
