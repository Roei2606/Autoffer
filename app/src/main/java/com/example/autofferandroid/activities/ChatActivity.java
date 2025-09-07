package com.example.autofferandroid.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autofferandroid.listeners.OnBoqAcceptListener;
import com.example.autofferandroid.listeners.OnBoqRejectListener;
import com.example.autofferandroid.listeners.OnBoqViewListener;
import com.example.chat_sdk.models.Message;
import com.example.chat_sdk.network.ChatManager;
import com.example.chat_sdk.network.MessageManager;
import com.example.chat_sdk.requests.UnreadCountRequest;
import com.example.autofferandroid.R;
import com.example.autofferandroid.adapters.MessageAdapter;
import com.example.autofferandroid.utils.BOQMessageHandler;
import com.example.projects_sdk.network.ProjectManager;
import com.example.rsocket_sdk.network.RSocketClientManager;
import com.example.rsocket_sdk.network.RSocketUtils;
import com.example.users_sdk.network.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import reactor.core.Disposable;
import reactor.core.scheduler.Schedulers;

public class ChatActivity extends AppCompatActivity
        implements OnBoqAcceptListener,
        OnBoqRejectListener,
        OnBoqViewListener {

    private String chatId;
    private String currentUserId;
    private String otherUserId;
    private RecyclerView recyclerView;
    private EditText inputMessage;
    private MaterialButton buttonSend;
    private MessageAdapter messageAdapter;
    private List<Message> messages;
    private ChatManager chatManager;
    private MessageManager messageManager;
    private Disposable messageStreamDisposable;
    private String currentUserProfileType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatId = getIntent().getStringExtra("chatId");
        currentUserId = getIntent().getStringExtra("currentUserId");
        otherUserId = getIntent().getStringExtra("otherUserId");

        if (chatId == null || currentUserId == null || otherUserId == null) {
            Log.e("ChatActivity", "Missing intent extras");
            finish();
            return;
        }

        currentUserProfileType = SessionManager.getInstance().getCurrentUser().getProfileType().toString();
        recyclerView = findViewById(R.id.recyclerViewMessages);
        inputMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(
                messages,
                currentUserId,
                currentUserProfileType,
                this,  // Accept listener
                this,  // Reject listener
                this   // View PDF listener
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        chatManager = new ChatManager();
        messageManager = new MessageManager();

        markMessagesAsRead();
        loadChatMessages();
        startListeningToStream();

        buttonSend.setOnClickListener(v -> {
            String content = inputMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                Message message = new Message(chatId, currentUserId, otherUserId, content, String.valueOf(System.currentTimeMillis()));
                message.setReadBy(new ArrayList<>());

                messageManager.sendMessage(message)
                        .thenAccept(sentMessage -> runOnUiThread(() -> {
                            messages.add(sentMessage);
                            messageAdapter.notifyItemInserted(messages.size() - 1);
                            recyclerView.scrollToPosition(messages.size() - 1);
                        }))
                        .exceptionally(e -> {
                            Log.e("ChatActivity", "❌ Failed to send message", e);
                            return null;
                        });

                inputMessage.setText("");
            }
        });
    }

    private void markMessagesAsRead() {
        try {
            RSocket rSocket = RSocketClientManager.getInstance().getRSocket();
            UnreadCountRequest request = new UnreadCountRequest(chatId, currentUserId);
            Payload payload = RSocketUtils.buildPayload("messages.markAsRead", request);

            rSocket.requestResponse(payload)
                    .doOnSuccess(p -> Log.d("ChatActivity", "✅ Marked messages as read"))
                    .doOnError(e -> Log.e("ChatActivity", "❌ Failed to mark messages as read", e))
                    .subscribe();
        } catch (Exception e) {
            Log.e("ChatActivity", "❌ markMessagesAsRead exception", e);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadChatMessages() {
        chatManager.getChatMessages(chatId, 0, 100)
                .thenAccept(loadedMessages -> runOnUiThread(() -> {
                    messages.clear();
                    messages.addAll(loadedMessages);
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size() - 1);
                }))
                .exceptionally(e -> {
                    Log.e("ChatActivity", "Error loading chat messages", e);
                    return null;
                });
    }


    private void startListeningToStream() {
        if (messageStreamDisposable != null && !messageStreamDisposable.isDisposed()) {
            messageStreamDisposable.dispose();
        }

        messageStreamDisposable = chatManager.streamMessages(chatId)
                .subscribe(newMessage -> runOnUiThread(() -> {
                    messages.add(newMessage);
                    messageAdapter.notifyItemInserted(messages.size() - 1);
                    recyclerView.scrollToPosition(messages.size() - 1);
                }), throwable -> Log.e("ChatActivity", "Error in message stream", throwable));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messageStreamDisposable != null && !messageStreamDisposable.isDisposed()) {
            messageStreamDisposable.dispose();
        }
    }

    // ✅ ממשקי פעולה עבור הודעת BOQ
    @Override
    public void onBoqAccepted(Message message) {
        BOQMessageHandler.handleApproval(this, message, true);
    }

    @Override
    public void onBoqRejected(Message message) {
        BOQMessageHandler.handleApproval(this, message, false);
    }

    @Override
    public void onBoqViewPdf(Message message) {
        BOQMessageHandler.openBoqPdf(this, message);
    }
}
