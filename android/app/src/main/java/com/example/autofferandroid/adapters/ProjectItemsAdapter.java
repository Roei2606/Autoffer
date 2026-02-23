
package com.example.autofferandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autofferandroid.R;
import com.example.local_project_sdk.models.LocalItemEntity;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class ProjectItemsAdapter extends RecyclerView.Adapter<ProjectItemsAdapter.ItemViewHolder> {

    public interface OnItemDeleteListener {
        void onItemDelete(LocalItemEntity item);
    }

    public interface OnItemEditListener {
        void onItemEdit(LocalItemEntity item);
    }

    private final List<LocalItemEntity> items;
    private final OnItemDeleteListener deleteListener;
    private final OnItemEditListener editListener;

    public ProjectItemsAdapter(List<LocalItemEntity> items,
                               OnItemDeleteListener deleteListener,
                               OnItemEditListener editListener) {
        this.items = items;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_current_item_of_project, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        LocalItemEntity item = items.get(position);

        holder.textProfileType.setText("Profile: " + item.getProfileNumber());
        holder.textGlassType.setText("Glass: " + item.getGlassType());
        holder.textDimensions.setText("Dimensions: " + (int) item.getHeight() + " x " + (int) item.getWidth() + " cm");
        holder.textLocation.setText("Location: " + item.getLocation());
        holder.textQuantity.setText("Quantity: " + item.getQuantity());
        holder.textPriceIndicator.setText(item.isExpensive() ? "$$" : "$");

        holder.buttonEdit.setOnClickListener(v -> {
            if (editListener != null) editListener.onItemEdit(item);
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onItemDelete(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView textProfileType, textGlassType, textDimensions, textLocation, textQuantity, textPriceIndicator;
        View buttonEdit, buttonDelete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textProfileType = itemView.findViewById(R.id.textProfileType);
            textGlassType = itemView.findViewById(R.id.textGlassType);
            textDimensions = itemView.findViewById(R.id.textDimensions);
            textLocation = itemView.findViewById(R.id.textLocation);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textPriceIndicator = itemView.findViewById(R.id.textPriceIndicator);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}

