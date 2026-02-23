package com.example.autofferandroid.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.autofferandroid.databinding.DialogEditItemBinding;
import com.example.local_project_sdk.models.LocalItemEntity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class EditItemDialog {

    public interface OnItemSavedListener {
        void onItemSaved(LocalItemEntity updatedItem);
    }

    private final Context context;
    private final LocalItemEntity originalItem;
    private final OnItemSavedListener listener;

    public EditItemDialog(Context context, LocalItemEntity item, OnItemSavedListener listener) {
        this.context = context;
        this.originalItem = item;
        this.listener = listener;
    }

    public void show() {
        DialogEditItemBinding binding = DialogEditItemBinding.inflate(LayoutInflater.from(context));
        binding.editQuantity.setText(String.valueOf(originalItem.getQuantity()));
        binding.editLocation.setText(originalItem.getLocation());
        binding.textDimensions.setText("Dimensions: " +
                (int) originalItem.getHeight() + " x " +
                (int) originalItem.getWidth() + " cm");

        Dialog dialog = new MaterialAlertDialogBuilder(context)
                .setView(binding.getRoot())
                .setCancelable(false)
                .create();

        binding.buttonSave.setOnClickListener(v -> {
            String quantityStr = binding.editQuantity.getText().toString().trim();
            String locationStr = binding.editLocation.getText().toString().trim();

            if (quantityStr.isEmpty() || locationStr.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            if (quantity <= 0) {
                Toast.makeText(context, "Quantity must be positive", Toast.LENGTH_SHORT).show();
                return;
            }

            LocalItemEntity updated = new LocalItemEntity();
            updated.setId(originalItem.getId());
            updated.setItemNumber(originalItem.getItemNumber());
            updated.setProfileNumber(originalItem.getProfileNumber());
            updated.setProfileType(originalItem.getProfileType());
            updated.setGlassType(originalItem.getGlassType());
            updated.setHeight(originalItem.getHeight());
            updated.setWidth(originalItem.getWidth());
            updated.setIsExpensive(originalItem.isExpensive());
            updated.setQuantity(quantity);
            updated.setLocation(locationStr);

            listener.onItemSaved(updated);
            dialog.dismiss();
        });

        binding.buttonCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
