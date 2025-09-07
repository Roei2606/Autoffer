package com.example.autofferandroid.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autofferandroid.R;
import com.example.autofferandroid.listeners.OnBoqAcceptListener;
import com.example.autofferandroid.listeners.OnBoqRejectListener;
import com.example.autofferandroid.listeners.OnBoqViewListener;
import com.example.autofferandroid.utils.QuoteMessageHandler;
import com.example.chat_sdk.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_RECEIVED = 0;
    private static final int TYPE_SENT = 1;
    private static final int TYPE_BOQ_REQUEST = 2;
    private static final int TYPE_QUOTE = 3;
    private static final int TYPE_HIDDEN = -1;

    private final List<Message> messageList;
    private final String currentUserId;
    private final String currentUserProfileType;
    private final OnBoqAcceptListener acceptListener;
    private final OnBoqRejectListener rejectListener;
    private final OnBoqViewListener viewListener;

    public MessageAdapter(List<Message> messageList, String currentUserId, String currentUserProfileType,
                          OnBoqAcceptListener acceptListener,
                          OnBoqRejectListener rejectListener,
                          OnBoqViewListener viewListener) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
        this.currentUserProfileType = currentUserProfileType;
        this.acceptListener = acceptListener;
        this.rejectListener = rejectListener;
        this.viewListener = viewListener;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);

        boolean isBoq = (message.getFileName() != null && message.getFileName().startsWith("BOQ_Project_"))
                || (message.getContent() != null && message.getContent().startsWith("[BOQ_REQUEST]"));

        boolean isQuote = (message.getFileName() != null && message.getFileName().startsWith("QUOTE_Project_"))
                || (message.getContent() != null && message.getContent().startsWith("[QUOTE_REQUEST]"));

        boolean isForCurrentUser = currentUserId.equals(message.getReceiverId());
        boolean isFactoryOrArchitect = "FACTORY".equals(currentUserProfileType) || "ARCHITECT".equals(currentUserProfileType);
        boolean isClient = "PRIVATE_CUSTOMER".equals(currentUserProfileType);

        if (isBoq && (!isFactoryOrArchitect || !isForCurrentUser)) return TYPE_HIDDEN;
        if (isQuote && !isClient) return TYPE_HIDDEN;

        if (message.getContent().startsWith("Dear Customer") && !isClient) return TYPE_HIDDEN;
        if ((message.getContent().startsWith("Your project has been approved by the factory") ||
                message.getContent().startsWith("Your project was declined by the factory")) && !isClient) {
            return TYPE_HIDDEN;
        }

        if (isBoq) return TYPE_BOQ_REQUEST;
        if (isQuote) return TYPE_QUOTE;

        return message.getSenderId().equals(currentUserId) ? TYPE_SENT : TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_HIDDEN) {
            View dummy = new View(parent.getContext());
            dummy.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            return new RecyclerView.ViewHolder(dummy) {};
        }

        if (viewType == TYPE_BOQ_REQUEST) {
            View view = inflater.inflate(R.layout.item_message_boq_request, parent, false);
            return new BoqMessageViewHolder(view);
        }

        if (viewType == TYPE_QUOTE) {
            View view = inflater.inflate(R.layout.item_message_quote, parent, false);
            return new QuoteMessageViewHolder(view);
        }

        int layout = (viewType == TYPE_SENT) ? R.layout.item_message_sent : R.layout.item_message_received;
        View view = inflater.inflate(layout, parent, false);
        return new TextMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (holder instanceof TextMessageViewHolder) {
            ((TextMessageViewHolder) holder).bind(message);
        } else if (holder instanceof BoqMessageViewHolder) {
            ((BoqMessageViewHolder) holder).bind(message);
        } else if (holder instanceof QuoteMessageViewHolder) {
            ((QuoteMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateMessages(List<Message> messages) {
        messageList.clear();
        messageList.addAll(messages);
        notifyDataSetChanged();
    }

    static class TextMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timestampText;

        public TextMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.textViewMessage);
            timestampText = itemView.findViewById(R.id.textViewTime);
        }

        public void bind(Message message) {
            messageText.setText(message.getContent());
            timestampText.setText(message.getTimestamp());
        }
    }

    class BoqMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textProjectAddress, textViewStatus;
        View buttonAccept, buttonReject, buttonViewPdf, layoutActions;

        public BoqMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textProjectAddress = itemView.findViewById(R.id.textViewProjectAddress);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            buttonAccept = itemView.findViewById(R.id.buttonAccept);
            buttonReject = itemView.findViewById(R.id.buttonReject);
            buttonViewPdf = itemView.findViewById(R.id.buttonViewPdf);
            layoutActions = itemView.findViewById(R.id.layoutActions);
        }

        public void bind(Message message) {
            textProjectAddress.setText("Project address: " + extractAddressFromContent(message.getContent()));

            buttonAccept.setOnClickListener(v -> {
                acceptListener.onBoqAccepted(message);
                layoutActions.setVisibility(View.GONE);
                textViewStatus.setVisibility(View.VISIBLE);
                textViewStatus.setText("ACCEPTED");
                textViewStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), android.R.color.holo_green_dark));
            });

            buttonReject.setOnClickListener(v -> {
                rejectListener.onBoqRejected(message);
                layoutActions.setVisibility(View.GONE);
                textViewStatus.setVisibility(View.VISIBLE);
                textViewStatus.setText("REJECTED");
                textViewStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), android.R.color.holo_red_dark));
            });

            buttonViewPdf.setOnClickListener(v -> viewListener.onBoqViewPdf(message));
        }

        private String extractAddressFromContent(String content) {
            return "N/A"; // אפשר לשפר בעתיד אם יכיל מידע אמיתי
        }
    }

    class QuoteMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textQuoteMessage;
        View buttonViewQuote;

        public QuoteMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textQuoteMessage = itemView.findViewById(R.id.textQuoteMessage);
            buttonViewQuote = itemView.findViewById(R.id.buttonViewQuotePdf);
        }

        public void bind(Message message) {
            textQuoteMessage.setText("You've received a quote for project: " + extractProjectName(message.getFileName()));
            buttonViewQuote.setOnClickListener(v -> QuoteMessageHandler.openQuotePdf(v.getContext(), message));
        }

        private String extractProjectName(String fileName) {
            if (fileName == null) return "Unknown";
            return fileName.replace("QUOTE_Project_", "").replace(".pdf", "");
        }
    }
}
