package com.example.autofferandroid.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.autofferandroid.R;
import com.example.autofferandroid.databinding.FragmentNewProjectBinding;
import com.example.local_project_sdk.db.LocalProjectStorage;
import com.example.local_project_sdk.models.LocalProjectEntity;
import com.example.local_project_sdk.repository.LocalProjectRepository;
import com.example.projects_sdk.models.MeasurementCamera;
import com.example.projects_sdk.network.ApiClient;
import com.example.projects_sdk.network.WindowMeasurementApi;
import com.example.users_sdk.network.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewProjectFragment extends Fragment {

    private FragmentNewProjectBinding binding;
    private LocalProjectRepository repository;
    private LocalProjectEntity currentProject;

    private Uri photoUri;
    private File photoFile;

    // ✅ הרשאת מצלמה
    private final ActivityResultLauncher<String> requestCameraPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(getContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    // ✅ מצלמה
    private final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success) {
                    sendImageToMeasurementService(photoFile);
                } else {
                    Toast.makeText(getContext(), "❌ Failed to take photo", Toast.LENGTH_SHORT).show();
                }
            });

    // ✅ גלריה
    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    try {
                        File file = createFileFromUri(uri);
                        sendImageToMeasurementService(file);
                    } catch (IOException e) {
                        Toast.makeText(getContext(), "❌ Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "❌ No image selected", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewProjectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        repository = new LocalProjectRepository(requireContext());

        binding.buttonAddManual.setOnClickListener(v -> {
            if (ensureProjectCreated()) {
                navigateToAddManualFragment();
            }
        });

        binding.buttonAddCamera.setOnClickListener(v -> {
            if (ensureProjectCreated()) {
                showImageSourceDialog();
            }
        });

        binding.buttonViewCurrentProject.setOnClickListener(v -> {
            if (LocalProjectStorage.getInstance().hasProject()) {
                navigateToCurrentProjectFragment();
            } else {
                new android.app.AlertDialog.Builder(requireContext())
                        .setTitle("No Project Found")
                        .setMessage("You haven't started a project yet. Please add at least one item first.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    // ✅ דיאלוג בחירה – מצלמה או גלריה
    private void showImageSourceDialog() {
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Select Image Source")
                .setItems(new CharSequence[]{"Camera", "Gallery"}, (dialog, which) -> {
                    if (which == 0) {
                        checkCameraPermissionAndOpen();
                    } else {
                        pickImageLauncher.launch("image/*");
                    }
                })
                .show();
    }

    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        try {
            photoFile = createImageFile();
            photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".provider",
                    photoFile
            );
            takePictureLauncher.launch(photoUri);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Error creating file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    // ✅ המרת URI של גלריה לקובץ זמני
    private File createFileFromUri(Uri uri) throws IOException {
        String fileName = getFileName(uri);
        File tempFile = new File(requireContext().getCacheDir(), fileName);

        try (InputStream in = requireContext().getContentResolver().openInputStream(uri);
             OutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
        return tempFile;
    }

    private String getFileName(Uri uri) {
        String result = "image_" + System.currentTimeMillis() + ".jpg";
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = requireContext().getContentResolver()
                    .query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        return result;
    }


    private void sendImageToMeasurementService(File file) {
        WindowMeasurementApi api = ApiClient
                .getClient("http://192.168.1.137:8010/") // אל תשכח סלאש בסוף
                .create(WindowMeasurementApi.class);


        String mimeType = requireContext().getContentResolver().getType(Uri.fromFile(file));
        if (mimeType == null) mimeType = "image/jpeg"; // fallback
        RequestBody reqFile = RequestBody.create(file, MediaType.parse(mimeType));

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);

        api.measureWindow(body).enqueue(new Callback<MeasurementCamera>() {
            @Override
            public void onResponse(Call<MeasurementCamera> call, Response<MeasurementCamera> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MeasurementCamera result = response.body();

                    // ✅ מעבר עם נתונים ל-AddManualFragment
                    Bundle args = new Bundle();
                    args.putInt("measured_width", (int) result.getWidth());
                    args.putInt("measured_height", (int) result.getHeight());

                    androidx.navigation.Navigation.findNavController(requireView())
                            .navigate(R.id.action_newProjectFragment_to_addManualFragment, args);

                } else {
                    Toast.makeText(getContext(), "⚠️ Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MeasurementCamera> call, Throwable t) {
                Toast.makeText(getContext(), "❌ Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean ensureProjectCreated() {
        String address = binding.inputProjectAddress.getText() != null
                ? binding.inputProjectAddress.getText().toString().trim() : "";

        if (TextUtils.isEmpty(address)) {
            binding.inputProjectAddress.setError("Project address is required");
            return false;
        }

        if (currentProject == null) {
            currentProject = new LocalProjectEntity();
            currentProject.setId(UUID.randomUUID().toString());
            currentProject.setProjectAddress(address);
            currentProject.setClientId(SessionManager.getInstance().getCurrentUserId());
            currentProject.setCreatedAt(getCurrentTimestamp());

            repository.insertProject(currentProject);
            LocalProjectStorage.getInstance().setCurrentProject(currentProject);

            binding.inputProjectAddress.setEnabled(false);
        }

        return true;
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private void navigateToAddManualFragment() {
        androidx.navigation.Navigation.findNavController(requireView())
                .navigate(R.id.action_newProjectFragment_to_addManualFragment);
    }

    private void navigateToCurrentProjectFragment() {
        androidx.navigation.Navigation.findNavController(requireView())
                .navigate(R.id.action_newProjectFragment_to_currentProjectFragment);
    }
}
