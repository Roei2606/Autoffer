package com.example.autofferandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.ads_sdk.models.Ad;
import com.example.autofferandroid.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.AdViewHolder> {

    private final List<Ad> ads;
    private final Context context;

    public AdsAdapter(List<Ad> ads, Context context) {
        this.ads = ads;
        this.context = context;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ad, parent, false);
        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        Ad ad = ads.get(position);

        holder.textTitle.setText(ad.getTitle());
        holder.textDescription.setText(ad.getDescription());
        Glide.with(context)
                .load(ad.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(holder.imageAd);

    }

    @Override
    public int getItemCount() {
        return ads.size();
    }

    public void setAds(List<Ad> newAds) {
        ads.clear();
        ads.addAll(newAds);
        notifyDataSetChanged();
    }

    public static class AdViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;
        ShapeableImageView imageAd;
        MaterialTextView textTitle;
        MaterialTextView textDescription;

        public AdViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardAd);
            imageAd = itemView.findViewById(R.id.imageAd);
            textTitle = itemView.findViewById(R.id.textAdTitle);
            textDescription = itemView.findViewById(R.id.textAdDescription);
        }
    }
}
