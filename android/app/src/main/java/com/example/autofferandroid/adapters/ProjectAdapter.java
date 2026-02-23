package com.example.autofferandroid.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autofferandroid.R;
import com.example.core_models_sdk.models.User;
import com.example.core_models_sdk.models.UserType;
import com.example.projects_sdk.models.ProjectDTO;
import com.example.users_sdk.network.SessionManager;
import com.example.users_sdk.network.UserManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    public interface OnProjectActionListener {
        void onViewBoqClicked(ProjectDTO project);
        void onCreateQuoteClicked(ProjectDTO project);
        void onSendQuoteClicked(ProjectDTO project);
        void onSendClicked(ProjectDTO project);
        void onDeleteClicked(ProjectDTO project);
    }

    private final List<ProjectDTO> projectList;
    private final OnProjectActionListener listener;
    private final String currentUserId;
    private final UserType profileType;
    private final boolean isFactory;
    private final UserManager userManager = new UserManager();
    private final Map<String, User> clientCache = new HashMap<>();

    public ProjectAdapter(List<ProjectDTO> projectList, OnProjectActionListener listener) {
        this.projectList = projectList;
        this.listener = listener;
        this.profileType = SessionManager.getInstance().getCurrentUserProfileType();
        this.currentUserId = SessionManager.getInstance().getCurrentUserId();
        if(profileType.equals(UserType.FACTORY)){
            this.isFactory=true;
        }else
            this.isFactory=false;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = isFactory ? R.layout.item_project_factory : R.layout.item_project;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        holder.bind(projectList.get(position));
    }
    @Override
    public int getItemCount() {
        return projectList != null ? projectList.size() : 0;
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView textProjectAddress, textItemCount, textCreatedAt;
        TextView textClientName, textClientPhone, textClientEmail, textFactoryStatus;
        View buttonViewPdf, buttonCreateQuote, buttonSendQuote, buttonSend, buttonDelete;
        LinearLayout containerFactoryStatuses;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            textProjectAddress = itemView.findViewById(R.id.textProjectAddress);
            textItemCount = itemView.findViewById(R.id.textItemCount);
            textCreatedAt = itemView.findViewById(R.id.textCreatedAt);
            textClientName = itemView.findViewById(R.id.textClientName);
            textClientPhone = itemView.findViewById(R.id.textClientPhone);
            textClientEmail = itemView.findViewById(R.id.textClientEmail);
            textFactoryStatus = itemView.findViewById(R.id.textFactoryStatus);
            containerFactoryStatuses = itemView.findViewById(R.id.containerFactoryStatuses);

            buttonViewPdf = itemView.findViewById(R.id.buttonViewBoqPdf);
            buttonCreateQuote = itemView.findViewById(R.id.buttonCreateQuote);
            buttonSendQuote = itemView.findViewById(R.id.buttonSendQuote);
            buttonSend = itemView.findViewById(R.id.buttonSend);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);


        }

        public void bind(ProjectDTO project) {
            textProjectAddress.setText(project.getProjectAddress());
            if (textItemCount != null)
                textItemCount.setText("Items: " + (project.getItems() != null ? project.getItems().size() : 0));
            if (textCreatedAt != null)
                textCreatedAt.setText("Created At: " + project.getCreatedAt());
            if (!isFactory && containerFactoryStatuses != null) {
                containerFactoryStatuses.removeAllViews();

                Map<String, String> quoteStatuses = project.getQuoteStatuses();
                if (quoteStatuses == null || quoteStatuses.isEmpty()) {
                    TextView warningText = new TextView(itemView.getContext());
                    warningText.setText("No factories received this project yet.");
                    warningText.setTextSize(14);
                    warningText.setPadding(10, 5, 10, 5);
                    containerFactoryStatuses.addView(warningText);
                } else {
                    for (Map.Entry<String, String> entry : quoteStatuses.entrySet()) {
                        String factoryId = entry.getKey();
                        String status = entry.getValue();

                        if (clientCache.containsKey(factoryId)) {
                            String factoryName = clientCache.get(factoryId).getFirstName() + " " + clientCache.get(factoryId).getLastName();
                            addFactoryStatusView(factoryName, status);
                        } else {
                            userManager.getUserById(factoryId).thenAccept(factoryUser -> {
                                clientCache.put(factoryId, factoryUser);
                                String factoryName = factoryUser.getFirstName() + " " + factoryUser.getLastName();
                                itemView.post(() -> addFactoryStatusView(factoryName, status));
                            });
                        }
                    }
                }
            }
            if (isFactory && textClientName != null) {
                String clientId = project.getClientId();
                if (clientId != null) {
                    if (clientCache.containsKey(clientId)) {
                        bindClientInfo(clientCache.get(clientId));
                    } else {
                        userManager.getUserById(clientId).thenAccept(user -> {
                            clientCache.put(clientId, user);
                            itemView.post(() -> bindClientInfo(user));
                        });
                    }
                }
            }
            if (isFactory && textFactoryStatus != null) {
                String status = project.getQuoteStatuses() != null ? project.getQuoteStatuses().get(currentUserId) : null;
                if (status != null) {
                    textFactoryStatus.setText("Status: " + status);
                    textFactoryStatus.setTextColor(getStatusColor(status));
                    textFactoryStatus.setVisibility(View.VISIBLE);
                } else {
                    textFactoryStatus.setVisibility(View.GONE);
                }
            }
            if (buttonViewPdf != null)
                buttonViewPdf.setOnClickListener(v -> listener.onViewBoqClicked(project));

            if (isFactory) {
                if (buttonCreateQuote != null)
                    buttonCreateQuote.setOnClickListener(v -> listener.onCreateQuoteClicked(project));
                if (buttonSendQuote != null)
                    buttonSendQuote.setOnClickListener(v -> listener.onSendQuoteClicked(project));
            } else {
                if (buttonSend != null)
                    buttonSend.setOnClickListener(v -> listener.onSendClicked(project));
                if (buttonDelete != null)
                    buttonDelete.setOnClickListener(v -> listener.onDeleteClicked(project));
            }
        }

        private void addFactoryStatusView(String factoryName, String status) {
            TextView statusView = new TextView(itemView.getContext());
            statusView.setText(factoryName + " → Status: " + status);
            statusView.setTextSize(14);
            statusView.setPadding(10, 5, 10, 5);
            statusView.setTextColor(getStatusColor(status));
            containerFactoryStatuses.addView(statusView);
        }

        private void bindClientInfo(User user) {
            if (textClientName != null) textClientName.setText("Client: " + user.getFirstName() + " " + user.getLastName());
            if (textClientPhone != null) textClientPhone.setText("Phone: " + user.getPhoneNumber());
            if (textClientEmail != null) textClientEmail.setText("Email: " + user.getEmail());
        }

        private int getStatusColor(String status) {
            switch (status.toUpperCase()) {
                case "ACCEPTED": return Color.parseColor("#388E3C");
                case "REJECTED": return Color.parseColor("#D32F2F");
                case "RECEIVED": return Color.parseColor("#1976D2");
                case "PENDING":
                default: return Color.parseColor("#F57C00");
            }
        }
    }
}
