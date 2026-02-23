package com.example.autofferandroid.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.autofferandroid.R;
import com.example.autofferandroid.utils.BitmapUtils;
import com.example.projects_sdk.models.Glass;

import java.util.List;

public class GlassAdapter extends RecyclerView.Adapter<GlassAdapter.GlassViewHolder> {

    public interface OnGlassClickListener {
        void onGlassSelected(Glass glass);
    }

    private List<Glass> glasses;
    private String recommendedType;
    private OnGlassClickListener listener;

    public GlassAdapter(List<Glass> glasses, String recommendedType, OnGlassClickListener listener) {
        this.glasses = glasses;
        this.recommendedType = recommendedType;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GlassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_glass_card, parent, false);
        return new GlassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GlassViewHolder holder, int position) {
        Glass glass = glasses.get(position);
        holder.textGlassType.setText(glass.getType());

        if (glass.getImageData() != null && !glass.getImageData().isEmpty()) {
            byte[] imageBytes = new byte[glass.getImageData().size()];
            for (int i = 0; i < imageBytes.length; i++) {
                imageBytes[i] = glass.getImageData().get(i);
            }
            Bitmap bitmap = BitmapUtils.decodeBase64ToBitmap(imageBytes);
            holder.imageGlass.setImageBitmap(bitmap);
        } else {
            holder.imageGlass.setImageResource(R.drawable.ic_placeholder); // תמונה ברירת מחדל
        }

        if (glass.getType().equalsIgnoreCase(recommendedType)) {
            holder.bannerRecommended.setVisibility(View.VISIBLE);
        } else {
            holder.bannerRecommended.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onGlassSelected(glass));
    }

    @Override
    public int getItemCount() {
        return glasses.size();
    }

    static class GlassViewHolder extends RecyclerView.ViewHolder {
        ImageView imageGlass;
        TextView textGlassType;
        TextView bannerRecommended;

        GlassViewHolder(@NonNull View itemView) {
            super(itemView);
            imageGlass = itemView.findViewById(R.id.imageGlass);
            textGlassType = itemView.findViewById(R.id.textGlassType);
            bannerRecommended = itemView.findViewById(R.id.bannerRecommended);
        }
    }
}
