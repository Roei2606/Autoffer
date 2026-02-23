package com.example.autofferandroid.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autofferandroid.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private FirebaseAuth firebaseAuth;
    private String verificationId;
    private CountDownTimer countDownTimer;
    private static final String TAG = "ForgotPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.sendCodeButton.setOnClickListener(v -> {
            String rawPhone = Objects.requireNonNull(binding.phoneNumberInput.getText()).toString().trim();
            if (rawPhone.isEmpty()) {
                binding.phoneNumberInput.setError("Phone number required");
                return;
            }

            String formattedPhone = formatPhoneNumberToE164(rawPhone);
            if (formattedPhone == null) {
                Toast.makeText(this, "Invalid Israeli phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            sendVerificationCode(formattedPhone);
        });

        binding.verifyCodeButton.setOnClickListener(v -> {
            String code = Objects.requireNonNull(binding.codeInput.getText()).toString().trim();
            if (code.isEmpty()) {
                binding.codeInput.setError("Verification code required");
                return;
            }
            verifyCode(code);
        });
    }

    private String formatPhoneNumberToE164(String rawPhone) {
        if (rawPhone.startsWith("0") && rawPhone.length() == 10) {
            return "+972" + rawPhone.substring(1);
        } else if (rawPhone.startsWith("+972") && rawPhone.length() == 13) {
            return rawPhone;
        } else {
            return null; // Invalid format
        }
    }

    private void sendVerificationCode(String formattedPhone) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(formattedPhone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        Toast.makeText(ForgotPasswordActivity.this, "Verification completed automatically", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.e(TAG, "Verification failed", e);
                        Toast.makeText(ForgotPasswordActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        verificationId = id;
                        Toast.makeText(ForgotPasswordActivity.this, "Code sent", Toast.LENGTH_SHORT).show();
                        startTimer();
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String code) {
        if (verificationId == null) {
            Toast.makeText(this, "Please request a verification code first", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Phone verified successfully", Toast.LENGTH_SHORT).show();
                        // 👉 Next: Navigate to ResetPasswordActivity
                    } else {
                        Toast.makeText(this, "Invalid verification code", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startTimer() {
        binding.sendCodeButton.setEnabled(false);
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.sendCodeButton.setText("Resend in " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                binding.sendCodeButton.setText("Send Code");
                binding.sendCodeButton.setEnabled(true);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) countDownTimer.cancel();
        super.onDestroy();
    }
}
