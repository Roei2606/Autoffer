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
import com.example.autofferandroid.adapters.AluminumProfileChoiceAdapter;
import com.example.autofferandroid.adapters.GlassAdapter;
import com.example.autofferandroid.databinding.FragmentAddManualBinding;
import com.example.local_project_sdk.db.LocalProjectStorage;
import com.example.local_project_sdk.models.LocalItemEntity;
import com.example.projects_sdk.models.AlumProfile;
import com.example.projects_sdk.models.AlumProfileDTO;
import com.example.projects_sdk.models.Glass;
import com.example.projects_sdk.models.GlassDTO;
import com.example.projects_sdk.network.ProjectManager;

import java.util.ArrayList;
import java.util.List;

public class AddManualFragment extends Fragment {

    private FragmentAddManualBinding binding;
    private ProjectManager projectManager;
    private AluminumProfileChoiceAdapter profileAdapter;
    private AlumProfile selectedProfile;
    private Glass selectedGlass;
    private final List<AlumProfile> profileList = new ArrayList<>();
    private final List<Glass> glassList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddManualBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        resetForm();
        projectManager = new ProjectManager();

        // ✅ קבלת ערכים מה-arguments (מועבר מ-NewProjectFragment אחרי צילום/גלריה)
        Bundle args = getArguments();
        if (args != null) {
            int measuredWidth = args.getInt("measured_width", -1);
            int measuredHeight = args.getInt("measured_height", -1);

            if (measuredWidth > 0) {
                binding.editTextWidth.setText(String.valueOf(measuredWidth));
            }
            if (measuredHeight > 0) {
                binding.editTextHeight.setText(String.valueOf(measuredHeight));
            }
        }

        binding.buttonFindProfiles.setOnClickListener(v -> {
            int width = parseInt(binding.editTextWidth.getText().toString());
            int height = parseInt(binding.editTextHeight.getText().toString());
            if (width <= 0 || height <= 0) {
                Toast.makeText(getContext(), "Please enter valid dimensions", Toast.LENGTH_SHORT).show();
                return;
            }

            showLoading(true);
            projectManager.getMatchingProfiles(width, height).thenAccept(profiles -> {
                requireActivity().runOnUiThread(() -> {
                    profileList.clear();
                    profileList.addAll(profiles);
                    showProfiles();
                });
            }).exceptionally(e -> {
                requireActivity().runOnUiThread(() -> {
                    showLoading(false);
                    Log.e("AddManualFragment", "Failed to load profiles: " + e.getMessage(), e);
                    Toast.makeText(getContext(), "Failed to load profiles", Toast.LENGTH_SHORT).show();
                });
                return null;
            });
        });

        binding.buttonAddItem.setOnClickListener(v -> {
            if (selectedProfile == null || selectedGlass == null) {
                Toast.makeText(getContext(), "Please select a profile and glass", Toast.LENGTH_SHORT).show();
                return;
            }

            String position = binding.editTextPosition.getText().toString().trim();
            String quantityText = binding.editTextQuantity.getText().toString().trim();
            if (position.isEmpty() || quantityText.isEmpty()) {
                Toast.makeText(getContext(), "Please enter position and quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = parseInt(quantityText);
            if (quantity <= 0) {
                Toast.makeText(getContext(), "Quantity must be positive", Toast.LENGTH_SHORT).show();
                return;
            }

            LocalItemEntity item = new LocalItemEntity();
            item.setItemNumber(LocalProjectStorage.getInstance().getCurrentItemEntities().size() + 1);
            item.setProfileNumber(selectedProfile.getProfileNumber());
            item.setProfileType(selectedProfile.getUsageType());
            item.setGlassType(selectedGlass.getType());
            item.setHeight(Double.parseDouble(binding.editTextHeight.getText().toString().trim()));
            item.setWidth(Double.parseDouble(binding.editTextWidth.getText().toString().trim()));
            item.setQuantity(quantity);
            item.setLocation(position);
            item.setIsExpensive(selectedProfile.isExpensive());

            AlumProfileDTO profileDTO = new AlumProfileDTO()
                    .setProfileNumber(selectedProfile.getProfileNumber())
                    .setUsageType(selectedProfile.getUsageType())
                    .setPricePerSquareMeter(selectedProfile.getPricePerSquareMeter());

            GlassDTO glassDTO = new GlassDTO()
                    .setType(selectedGlass.getType())
                    .setPricePerSquareMeter(selectedGlass.getPricePerSquareMeter());

            item.setProfile(profileDTO);
            item.setGlass(glassDTO);

            LocalProjectStorage.getInstance().addItemToCurrentProject(item);
            showSuccessDialog();
        });
    }

    private void showProfiles() {
        showLoading(false);

        for (AlumProfile p : profileList) {
            Log.d("AddManualFragment", "Loaded profile: " + p.getProfileNumber() + ", Type: " + p.getUsageType());
        }

        if (profileAdapter == null) {
            profileAdapter = new AluminumProfileChoiceAdapter(profileList, profile -> {
                selectedProfile = profile;
                loadGlasses(profile.getProfileNumber(), profile.getRecommendedGlassType());
            });
        } else {
            profileAdapter.updateProfiles(profileList);
        }

        if (binding.recyclerProfiles.getLayoutManager() == null) {
            binding.recyclerProfiles.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        binding.recyclerProfiles.setAdapter(profileAdapter);
        binding.recyclerProfiles.setVisibility(View.VISIBLE);
    }

    private void loadGlasses(String profileNumber, String recommended) {
        showLoading(true);
        projectManager.getGlassesForProfile(profileNumber).thenAccept(glasses -> {
            requireActivity().runOnUiThread(() -> {
                GlassAdapter adapter = new GlassAdapter(glasses, recommended, glass -> {
                    selectedGlass = glass;
                    binding.inputLayoutPosition.setVisibility(View.VISIBLE);
                    binding.inputLayoutQuantity.setVisibility(View.VISIBLE);
                    binding.buttonAddItem.setVisibility(View.VISIBLE);
                });
                binding.recyclerGlasses.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                binding.recyclerGlasses.setAdapter(adapter);
                binding.recyclerGlasses.setVisibility(View.VISIBLE);
                showLoading(false);
            });
        }).exceptionally(e -> {
            requireActivity().runOnUiThread(() -> {
                showLoading(false);
                Toast.makeText(getContext(), "Failed to load glasses", Toast.LENGTH_SHORT).show();
            });
            return null;
        });
    }

    private void showLoading(boolean isLoading) {
        binding.loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.scrollContainer.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void showSuccessDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Item Added")
                .setMessage("The item was added successfully. Do you want to add another one?")
                .setPositiveButton("Yes", (dialog, which) ->
                        requireActivity().getSupportFragmentManager().popBackStack())
                .setNegativeButton("No", (dialog, which) -> {
                    NavController navController = NavHostFragment.findNavController(AddManualFragment.this);
                    navController.navigate(R.id.action_addManualFragment_to_currentProjectFragment);
                })
                .show();
    }

    private void resetForm() {
        binding.editTextHeight.setText("");
        binding.editTextWidth.setText("");
        binding.editTextPosition.setText("");
        binding.editTextQuantity.setText("");
        selectedProfile = null;
        selectedGlass = null;
        profileList.clear();
        glassList.clear();
        if (profileAdapter != null) {
            profileAdapter.updateProfiles(new ArrayList<>());
        }
        binding.recyclerProfiles.setAdapter(null);
        binding.recyclerProfiles.setVisibility(View.GONE);
        binding.recyclerGlasses.setAdapter(null);
        binding.recyclerGlasses.setVisibility(View.GONE);
        binding.inputLayoutPosition.setVisibility(View.GONE);
        binding.inputLayoutQuantity.setVisibility(View.GONE);
        binding.buttonAddItem.setVisibility(View.GONE);
    }

    private int parseInt(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
