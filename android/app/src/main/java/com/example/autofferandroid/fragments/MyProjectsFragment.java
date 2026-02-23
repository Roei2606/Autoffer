package com.example.autofferandroid.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.autofferandroid.activities.PdfViewActivity;
import com.example.autofferandroid.adapters.ProjectAdapter;
import com.example.autofferandroid.databinding.FragmentMyProjectsBinding;
import com.example.autofferandroid.dialogs.FactorySelectionDialog;
import com.example.autofferandroid.utils.TimeUtils;
import com.example.chat_sdk.models.Message;
import com.example.chat_sdk.network.ChatManager;
import com.example.chat_sdk.network.MessageManager;
import com.example.chat_sdk.requests.FileMessageRequest;
import com.example.core_models_sdk.models.FactoryUser;
import com.example.core_models_sdk.models.UserType;
import com.example.projects_sdk.models.ProjectDTO;
import com.example.projects_sdk.models.Quote;
import com.example.projects_sdk.models.QuoteStatus;
import com.example.projects_sdk.network.ProjectManager;
import com.example.projects_sdk.requests.UpdateFactoryStatusRequest;
import com.example.projects_sdk.requests.UserProjectRequest;
import com.example.users_sdk.network.SessionManager;
import com.example.core_models_sdk.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyProjectsFragment extends Fragment {

    private FragmentMyProjectsBinding binding;
    private ProjectAdapter projectAdapter;
    private ProjectManager projectManager;
    private MessageManager messageManager;
    private ChatManager chatManager;
    private final List<ProjectDTO> projectList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyProjectsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        projectManager = new ProjectManager();
        messageManager = new MessageManager();
        chatManager = new ChatManager();
        setupAdapter();
    }

    private void setupAdapter() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserId = currentUser.getId();

        projectAdapter = new ProjectAdapter(
                projectList,
                new ProjectAdapter.OnProjectActionListener() {
                    @Override
                    public void onViewBoqClicked(ProjectDTO project) {
                        if (project.getBoqPdf() == null || project.getBoqPdf().isEmpty()) {
                            Toast.makeText(getContext(), "No BOQ PDF available", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(requireContext(), PdfViewActivity.class);
                        intent.putExtra(PdfViewActivity.EXTRA_PDF_NAME, "BOQ_" + project.getProjectId() + ".pdf");
                        intent.putExtra(PdfViewActivity.EXTRA_PDF_BYTES, new ArrayList<>(project.getBoqPdf()));
                        startActivity(intent);
                    }

                    @Override
                    public void onCreateQuoteClicked(ProjectDTO project) {
                        Map<String, Quote> quotes = project.getQuotes();
                        String currentFactoryId = SessionManager.getInstance().getCurrentUser().getId();

                        if (quotes == null || !quotes.containsKey(currentFactoryId)) {
                            Toast.makeText(getContext(), "No quote available for this factory", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Quote quote = quotes.get(currentFactoryId);
                        if (quote.getQuotePdf() == null || quote.getQuotePdf().isEmpty()) {
                            Toast.makeText(getContext(), "Quote PDF is missing", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent(requireContext(), PdfViewActivity.class);
                        intent.putExtra(PdfViewActivity.EXTRA_PDF_NAME, "QUOTE_" + project.getProjectId() + ".pdf");
                        intent.putExtra(PdfViewActivity.EXTRA_PDF_BYTES, new ArrayList<>(quote.getQuotePdf()));
                        startActivity(intent);
                    }


                    @Override
                    public void onSendQuoteClicked(ProjectDTO project) {
                        String factoryId = SessionManager.getInstance().getCurrentUserId();
                        String factoryName = SessionManager.getInstance().getCurrentUser().getFirstName() + " " +
                                SessionManager.getInstance().getCurrentUser().getLastName();
                        String clientId = project.getClientId();

                        Quote quote = project.getQuotes().get(factoryId);

                        if (quote == null || quote.getQuotePdf() == null || quote.getQuotePdf().isEmpty()) {
                            Toast.makeText(getContext(), "No quote PDF found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // קבל או צור צ׳אט ואז שלח הודעה
                        chatManager.getOrCreateChat(factoryId, clientId)
                                .thenAccept(chat -> {
                                    String chatId = chat.getId();

                                    Message fakeQuoteMessage = new Message();
                                    fakeQuoteMessage.setChatId(chatId);
                                    fakeQuoteMessage.setSenderId(factoryId);
                                    fakeQuoteMessage.setReceiverId(clientId);
                                    fakeQuoteMessage.setContent("A quote has been sent for your project by " + factoryName);
                                    fakeQuoteMessage.setFileName("QUOTE_Project_" + project.getProjectId() + ".pdf");
                                    fakeQuoteMessage.setTimestamp(TimeUtils.getCurrentFormattedTime());
                                    fakeQuoteMessage.setFileType("PDF");
                                    fakeQuoteMessage.setReadBy(new ArrayList<>());
                                    fakeQuoteMessage.setType("QUOTE");

                                    messageManager.sendMessage(fakeQuoteMessage)
                                            .thenAccept(v -> {
                                                Toast.makeText(getContext(), "Quote message sent", Toast.LENGTH_SHORT).show();
                                                projectManager.updateFactoryStatus(
                                                        project.getProjectId(),
                                                        factoryId,
                                                        QuoteStatus.RECEIVED
                                                ).subscribe();
                                            })
                                            .exceptionally(e -> {
                                                Toast.makeText(getContext(), "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                return null;
                                            });
                                })
                                .exceptionally(e -> {
                                    Toast.makeText(getContext(), "Failed to create chat: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    return null;
                                });
                    }


                    @Override
                    public void onSendClicked(ProjectDTO project) {
                        if (project.getBoqPdf() == null || project.getBoqPdf().isEmpty()) {
                            Toast.makeText(getContext(), "Project must have a BOQ PDF before sending", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        FactorySelectionDialog.show(requireContext(), project, () -> {
                            Toast.makeText(getContext(), "BOQ sent successfully", Toast.LENGTH_SHORT).show();
                            loadProjects(currentUserId);
                        });
                    }

                    @Override
                    public void onDeleteClicked(ProjectDTO project) {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Delete Project")
                                .setMessage("Are you sure you want to delete this project?")
                                .setPositiveButton("Delete", (dialog, which) -> {
                                    projectManager.deleteProjectById(project.getProjectId()).thenRun(() ->
                                            requireActivity().runOnUiThread(() -> {
                                                Toast.makeText(getContext(), "Project deleted", Toast.LENGTH_SHORT).show();
                                                loadProjects(currentUserId);
                                            })
                                    ).exceptionally(e -> {
                                        requireActivity().runOnUiThread(() ->
                                                Toast.makeText(getContext(), "Failed to delete project", Toast.LENGTH_SHORT).show()
                                        );
                                        return null;
                                    });
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                }
        );

        binding.recyclerViewProjects.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewProjects.setAdapter(projectAdapter);

        loadProjects(currentUserId);
    }

    private void loadProjects(String userId) {
        showLoading(true);
        fetchProjects(userId);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchProjects(String userId) {
       User currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser == null) {
             Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
             showLoading(false);
              return;
    }

    UserProjectRequest request = new UserProjectRequest(userId, currentUser.getProfileType());

    projectManager.getUserProjects(request).thenAccept(projects -> {
        requireActivity().runOnUiThread(() -> {
            projectList.clear();
            projectList.addAll(projects);

            boolean hasProjects = !projects.isEmpty();
            binding.textViewNoProjects.setVisibility(hasProjects ? View.GONE : View.VISIBLE);
            binding.recyclerViewProjects.setVisibility(hasProjects ? View.VISIBLE : View.GONE);

            projectAdapter.notifyDataSetChanged();
            showLoading(false);
        });
    }).exceptionally(e -> {
        requireActivity().runOnUiThread(() -> {
            Log.e("MyProjectsFragment", "❌ Failed to load projects", e);
            Toast.makeText(getContext(), "Failed to load projects", Toast.LENGTH_SHORT).show();
            showLoading(false);
        });
        return null;
    });
}

    private void showLoading(boolean isLoading) {
        binding.loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.recyclerViewProjects.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}
