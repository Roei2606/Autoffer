
package com.example.autofferandroid.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.autofferandroid.R;
import com.example.autofferandroid.adapters.ProjectItemsAdapter;
import com.example.autofferandroid.databinding.FragmentCurrentProjectBinding;
import com.example.autofferandroid.dialogs.EditItemDialog;
import com.example.local_project_sdk.db.LocalProjectStorage;
import com.example.local_project_sdk.mappers.CreateProjectMapper;
import com.example.local_project_sdk.models.LocalItemEntity;
import com.example.local_project_sdk.models.LocalProjectEntity;
import com.example.projects_sdk.network.ProjectManager;
import com.example.projects_sdk.requests.CreateProjectRequest;

import java.util.List;

public class CurrentProjectFragment extends Fragment {

    private FragmentCurrentProjectBinding binding;
    private List<LocalItemEntity> currentItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCurrentProjectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        currentItems = LocalProjectStorage.getInstance().getCurrentItemEntities();

        if (currentItems == null || currentItems.isEmpty()) {
            showEmptyDialog();
            return;
        }

        setupRecyclerView();
        setupButtons();
    }

    private void setupRecyclerView() {
        ProjectItemsAdapter adapter = new ProjectItemsAdapter(
                currentItems,
                this::showDeleteDialog,
                this::showEditDialog
        );
        binding.recyclerProjectItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerProjectItems.setAdapter(adapter);
    }

    private void showEditDialog(LocalItemEntity item) {
        new EditItemDialog(requireContext(), item, updatedItem -> {
            for (int i = 0; i < currentItems.size(); i++) {
                if (currentItems.get(i).getItemNumber() == updatedItem.getItemNumber()) {
                    currentItems.set(i, updatedItem);
                    break;
                }
            }
            Toast.makeText(requireContext(), "Item updated", Toast.LENGTH_SHORT).show();
            setupRecyclerView();
        }).show();
    }

    private void showDeleteDialog(LocalItemEntity item) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    currentItems.remove(item);
                    Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                    setupRecyclerView();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setupButtons() {
        binding.buttonAddAnotherProduct.setOnClickListener(v -> {
            NavController navController = NavHostFragment.findNavController(CurrentProjectFragment.this);
            navController.navigate(R.id.action_currentProjectFragment_to_addManualFragment);

        });


        binding.buttonSaveProject.setOnClickListener(v -> {
            LocalProjectEntity project = LocalProjectStorage.getInstance().getCurrentProject();

            if (project == null || project.getProjectAddress() == null || project.getProjectAddress().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Project address missing", Toast.LENGTH_SHORT).show();
                return;
            }

            CreateProjectRequest request = CreateProjectMapper.toCreateRequest(project, currentItems);

            new ProjectManager().createProject(request).thenAccept(serverProject -> {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(requireContext(), "Project saved successfully", Toast.LENGTH_SHORT).show();
                            LocalProjectStorage.getInstance().clear();

                            NavController navController = NavHostFragment.findNavController(CurrentProjectFragment.this);
                            navController.navigate(R.id.nav_my_projects);
                        });
                    }).exceptionally(e -> {requireActivity().runOnUiThread(() -> {
                    Log.e("CurrentProjectFragment", "Failed to save project", e);
                    Toast.makeText(requireContext(), "Failed to save project", Toast.LENGTH_SHORT).show();
                });
                return null;
            });
        });
    }

    private void showEmptyDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("No Products")
                .setMessage("No items have been added to this project yet. Please add at least one.")
                .setPositiveButton("OK", (dialog, which) ->
                        requireActivity().getSupportFragmentManager().popBackStack()
                )
                .setCancelable(false)
                .show();
    }
}
