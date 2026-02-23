package com.example.autofferandroid.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autofferandroid.databinding.ItemUserFactoryCardBinding;
import com.example.autofferandroid.databinding.ItemUserGenericCardBinding;
import com.example.core_models_sdk.models.FactoryUser;
import com.example.core_models_sdk.models.User;
import com.example.core_models_sdk.models.UserType;


import java.util.List;

public class UserCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<User> users;

    private static final int TYPE_FACTORY = 0;
    private static final int TYPE_ARCHITECT = 1;

    public UserCardAdapter(List<User> users, Context context) {
        this.users = users;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<User> newUsers) {
        users.clear();
        users.addAll(newUsers);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        try {
            UserType type = users.get(position).getProfileType();
            return type == UserType.FACTORY ? TYPE_FACTORY : TYPE_ARCHITECT;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return TYPE_ARCHITECT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FACTORY) {
            ItemUserFactoryCardBinding binding = ItemUserFactoryCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new FactoryViewHolder(binding);
        } else {
            ItemUserGenericCardBinding binding = ItemUserGenericCardBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ArchitectViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = users.get(position);
        if (holder instanceof FactoryViewHolder) {
            ((FactoryViewHolder) holder).bind((FactoryUser) user);  // cast בטוח אם השדה נכון
        } else if (holder instanceof ArchitectViewHolder) {
            ((ArchitectViewHolder) holder).bind(user);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class FactoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserFactoryCardBinding binding;
        private boolean expanded = false;

        FactoryViewHolder(ItemUserFactoryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        @SuppressLint("SetTextI18n")
        void bind(FactoryUser factoryUser) {
            binding.textFactoryName.setText(factoryUser.getFactoryName());
            binding.textFactoryAddress.setText(factoryUser.getAddress());
            binding.textBusinessId.setText("Business ID: " + factoryUser.getBusinessId());

            binding.contactSection.setVisibility(View.GONE);

            binding.buttonShowContact.setOnClickListener(v -> {
                expanded = !expanded;
                binding.contactSection.setVisibility(expanded ? View.VISIBLE : View.GONE);
                binding.buttonShowContact.setText(expanded ? "Hide Contact Info" : "Show Contact Info");
                binding.textContactName.setText(factoryUser.getFirstName() + " " + factoryUser.getLastName());
                binding.textContactEmail.setText(factoryUser.getEmail());
                binding.textContactPhone.setText(factoryUser.getPhoneNumber());
            });

            binding.buttonOpenChat.setOnClickListener(v -> {
                // ✉️ לקרוא לפונקציה לפתיחת צ'אט עם factoryUser.getId()
            });
        }
    }

    static class ArchitectViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserGenericCardBinding binding;

        ArchitectViewHolder(ItemUserGenericCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void bind(User user) {
            binding.textUserName.setText(user.getFirstName() + " " + user.getLastName());
            binding.textUserAddress.setText(user.getAddress());

            binding.buttonSendMessage.setOnClickListener(v -> {
                // ✉️ לקרוא לפונקציה לפתיחת צ'אט עם user.getId()
            });
        }
    }
}
