package com.example.autofferandroid.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ads_sdk.models.Ad;
import com.example.ads_sdk.network.AdsManager;
import com.example.autofferandroid.R;
import com.example.autofferandroid.adapters.AdsAdapter;
import com.example.autofferandroid.databinding.FragmentHomeBinding;
import com.example.autofferandroid.transformers.DoorOpenPageTransformer;
import com.example.autofferandroid.utils.BitmapUtils;
import com.example.core_models_sdk.models.User;
import com.example.core_models_sdk.models.UserType;
import com.example.users_sdk.network.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AdsAdapter adsAdapter;
    private AdsManager adsManager;
    private final List<Ad> adList = new ArrayList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable autoScrollRunnable;
    private static final long AUTO_SCROLL_DELAY = 4000;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adsManager = new AdsManager();
        adsAdapter = new AdsAdapter(adList, requireContext());
        binding.viewPagerAds.setAdapter(adsAdapter);
        binding.viewPagerAds.setPageTransformer(new DoorOpenPageTransformer());

        setupUserDetails();
        loadAds();
    }

    private void setupUserDetails() {
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            binding.textViewUserName.setText("Hello, " + currentUser.getFirstName() + "!");
            binding.textViewUserType.setText(currentUser.getProfileType().name().replace("_", " "));

            byte[] logoBytes = currentUser.getPhotoBytes(); // assume logo is byte[] from Mongo
            if (logoBytes != null && logoBytes.length > 0) {
                binding.imageViewProfile.setImageBitmap(BitmapUtils.decodeBase64ToBitmap(logoBytes));
            } else {
                binding.imageViewProfile.setImageResource(R.drawable.ic_placeholder_user);
            }

        } else {
            binding.textViewUserName.setText("Hello, Guest!");
            binding.textViewUserType.setText("Unknown Type");
            binding.imageViewProfile.setImageResource(R.drawable.ic_placeholder_user);
        }
    }

    private void loadAds() {
        showLoading(true);

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            showLoading(false);
            return;
        }

        try {
            UserType profileType = currentUser.getProfileType();

            CompletableFuture<List<Ad>> future = adsManager.getAdsForAudience(profileType);
            future.thenAccept(ads -> {
                requireActivity().runOnUiThread(() -> {
                    adList.clear();
                    adList.addAll(ads);
                    adsAdapter.notifyDataSetChanged();
                    showLoading(false);
                    startAutoScroll();
                });
            }).exceptionally(e -> {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> showLoading(false));
                return null;
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            showLoading(false);
        }
    }

    private void showLoading(boolean isLoading) {
        if (binding == null) return;
        binding.loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.viewPagerAds.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void startAutoScroll() {
        stopAutoScroll();
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (binding != null && binding.viewPagerAds != null && adsAdapter != null) {
                    int currentItem = binding.viewPagerAds.getCurrentItem();
                    int itemCount = adsAdapter.getItemCount();
                    if (itemCount > 0) {
                        int nextItem = (currentItem + 1) % itemCount;
                        binding.viewPagerAds.setCurrentItem(nextItem, true);
                        handler.postDelayed(this, AUTO_SCROLL_DELAY);
                    }
                }
            }
        };
        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    private void stopAutoScroll() {
        if (autoScrollRunnable != null) {
            handler.removeCallbacks(autoScrollRunnable);
        }
    }

    @Override
    public void onDestroyView() {
        stopAutoScroll();
        binding = null;
        super.onDestroyView();
    }
}
