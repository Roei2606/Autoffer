package com.example.autofferandroid.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.autofferandroid.R;
import com.example.autofferandroid.databinding.FragmentForgotPasswordBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ForgotPasswordFragment extends Fragment {

    private FragmentForgotPasswordBinding binding;
    private FirebaseAuth firebaseAuth;
    private String verificationId;
    private CountDownTimer countDownTimer;
    private static final String TAG = "ForgotPassword";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();

        binding.sendCodeButton.setOnClickListener(v -> {
            String rawPhone = Objects.requireNonNull(binding.phoneNumberInput.getText()).toString().trim();
            if (rawPhone.isEmpty()) {
                binding.phoneNumberInput.setError("Phone number required");
                return;
            }

            String formattedPhone = formatPhoneNumberToE164(rawPhone);
            if (formattedPhone == null) {
                Toast.makeText(requireContext(), "Invalid Israeli phone number", Toast.LENGTH_SHORT).show();
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
            return null;
        }
    }

    private void sendVerificationCode(String formattedPhone) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(formattedPhone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        Toast.makeText(requireContext(), "Verification completed automatically", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.e(TAG, "Verification failed", e);
                        Toast.makeText(requireContext(), "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        verificationId = id;
                        Toast.makeText(requireContext(), "Code sent", Toast.LENGTH_SHORT).show();
                        startTimer();
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String code) {
        if (verificationId == null) {
            Toast.makeText(requireContext(), "Please request a verification code first", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireContext(), "Phone verified successfully", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(binding.getRoot())
                                .navigate(R.id.action_forgotPasswordFragment_to_resetPasswordFragment);
                    } else {
                        Toast.makeText(requireContext(), "Invalid verification code", Toast.LENGTH_SHORT).show();
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
    public void onDestroyView() {
        if (countDownTimer != null) countDownTimer.cancel();
        super.onDestroyView();
        binding = null;
    }
}
