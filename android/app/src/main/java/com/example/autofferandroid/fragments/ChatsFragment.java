package com.example.autofferandroid.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_sdk.models.Chat;
import com.example.chat_sdk.network.ChatManager;
import com.example.autofferandroid.R;
import com.example.autofferandroid.activities.ChatActivity;
import com.example.autofferandroid.adapters.ChatListAdapter;
import com.example.users_sdk.network.SessionManager;
import com.example.users_sdk.network.UserManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class ChatsFragment extends Fragment {

    private ChatListAdapter chatAdapter;
    private final List<ChatListAdapter.ChatDisplayItem> chatItems = new ArrayList<>();
    private final ChatManager chatManager = new ChatManager();
    private final UserManager userManager = new UserManager();

    private TextView textViewNoChats;
    private RecyclerView recyclerView;

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewChats);
        textViewNoChats = view.findViewById(R.id.textViewNoChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chatAdapter = new ChatListAdapter(chatItems, item -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("chatId", item.chat.getId());
            intent.putExtra("otherUserId", item.otherUserId);
            intent.putExtra("currentUserId", SessionManager.getInstance().getCurrentUserId());
            startActivity(intent);
        });
        recyclerView.setAdapter(chatAdapter);

        checkIfHasChats();

        return view;
    }

    private void checkIfHasChats() {
        String currentUserId = SessionManager.getInstance().getCurrentUserId();
        chatManager.hasChats(currentUserId)
                .thenAccept(hasChats -> {
                    if (getActivity() == null) return; // למנוע crash אם הפרגמנט כבר לא פעיל
                    requireActivity().runOnUiThread(() -> {
                        if (hasChats) {
                            textViewNoChats.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            loadChats();
                        } else {
                            textViewNoChats.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    });
                })
                .exceptionally(e -> {
                    Log.e("ChatsFragment", "❌ Failed to check chats", e);
                    if (getActivity() != null) {
                        requireActivity().runOnUiThread(() -> {
                            textViewNoChats.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Error loading chats", Toast.LENGTH_SHORT).show();
                        });
                    }
                    return null;
                });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadChats() {
        String currentUserId = SessionManager.getInstance().getCurrentUserId();
        chatManager.getUserChats(currentUserId, 0, 50).thenAccept(chats -> {
            List<CompletableFuture<ChatListAdapter.ChatDisplayItem>> futures = new ArrayList<>();

            for (Chat chat : chats) {
                String otherUserId = chat.getParticipants().stream()
                        .filter(id -> !currentUserId.equals(id))
                        .findFirst()
                        .orElse(null);
                if (otherUserId == null) continue;

                CompletableFuture<ChatListAdapter.ChatDisplayItem> itemFuture =
                        userManager.getUserById(otherUserId)
                                .thenCombine(chatManager.getUnreadCount(chat.getId(), currentUserId),
                                        (user, count) -> {
                                            if (user == null) return null;
                                            String formattedTime = formatTimestamp(chat.getLastMessageTimestamp());
                                            return new ChatListAdapter.ChatDisplayItem(chat, user.getFirstName(), otherUserId, formattedTime, count);
                                        })
                                .exceptionally(e -> null);

                futures.add(itemFuture);
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenRun(() -> {
                        List<ChatListAdapter.ChatDisplayItem> result = new ArrayList<>();
                        for (CompletableFuture<ChatListAdapter.ChatDisplayItem> future : futures) {
                            try {
                                ChatListAdapter.ChatDisplayItem item = future.get();
                                if (item != null) result.add(item);
                            } catch (Exception ignored) {}
                        }

                        requireActivity().runOnUiThread(() -> {
                            chatItems.clear();
                            chatItems.addAll(result);
                            chatAdapter.notifyDataSetChanged();
                        });
                    });
        });
    }

    private String formatTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) return "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(timestamp);
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            return "";
        }
    }
}
