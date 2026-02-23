package com.example.autofferandroid.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.autofferandroid.R;
import com.example.autofferandroid.utils.BitmapUtils;
import com.example.projects_sdk.models.AlumProfile;

import java.util.ArrayList;
import java.util.List;

public class AluminumProfileChoiceAdapter extends RecyclerView.Adapter<AluminumProfileChoiceAdapter.ProfileViewHolder> {

    public interface OnProfileClickListener {
        void onProfileSelected(AlumProfile profile);
    }

    private final List<AlumProfile> profiles = new ArrayList<>();
    private final OnProfileClickListener listener;

    public AluminumProfileChoiceAdapter(List<AlumProfile> initialProfiles, OnProfileClickListener listener) {
        if (initialProfiles != null) {
            this.profiles.addAll(initialProfiles);
        }
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aluminum_profile_card, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        AlumProfile profile = profiles.get(position);
        holder.textProfileNumber.setText(profile.getProfileNumber());
        holder.textUsageType.setText(profile.getUsageType().toString().replace("_", " "));
        holder.textPriceLevel.setText(profile.isExpensive() ? "$$" : "$");

        if (profile.getImageData() != null && profile.getImageData().length > 0) {
            holder.imageProfile.setImageBitmap(BitmapUtils.decodeBase64ToBitmap(profile.getImageData()));
        }

        holder.itemView.setOnClickListener(v -> listener.onProfileSelected(profile));
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateProfiles(List<AlumProfile> newProfiles) {
        profiles.clear();
        if (newProfiles != null) {
            profiles.addAll(newProfiles);
        }
        notifyDataSetChanged();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView textProfileNumber, textUsageType, textPriceLevel;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textProfileNumber = itemView.findViewById(R.id.textProfileNumber);
            textUsageType = itemView.findViewById(R.id.textUsageType);
            textPriceLevel = itemView.findViewById(R.id.textPriceLevel);
        }
    }
}
