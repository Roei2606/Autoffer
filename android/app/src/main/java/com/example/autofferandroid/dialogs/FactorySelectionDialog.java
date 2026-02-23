package com.example.autofferandroid.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.example.core_models_sdk.models.FactoryUser;
import com.example.core_models_sdk.models.User;
import com.example.core_models_sdk.models.UserType;
import com.example.projects_sdk.models.ProjectDTO;
import com.example.projects_sdk.network.ProjectManager;
import com.example.projects_sdk.requests.SendBOQRequest;
import com.example.users_sdk.network.UserManager;

import java.util.ArrayList;
import java.util.List;

public class FactorySelectionDialog {

    public static void show(Context context, ProjectDTO project, Runnable onSuccess) {
        UserManager userManager = new UserManager();

        userManager.getAllUsers().thenAccept(users -> {
            List<User> factoryUsers = new ArrayList<>();
            List<String> factoryNames = new ArrayList<>();

            for (User user : users) {
                if (user.getProfileType()== UserType.FACTORY) {
                    factoryUsers.add(user);

                    String displayName;
                    if (user instanceof FactoryUser) {
                        FactoryUser factory = (FactoryUser) user;
                        displayName = factory.getFactoryName() != null
                                ? factory.getFactoryName() + " (" + factory.getFirstName() + " " + factory.getLastName() + ")"
                                : factory.getFirstName() + " " + factory.getLastName();
                    } else {
                        displayName = user.getFirstName() + " " + user.getLastName();
                    }

                    factoryNames.add(displayName);
                }
            }

            if (factoryUsers.isEmpty()) {
                ((Activity) context).runOnUiThread(() ->
                        Toast.makeText(context, "No factories found", Toast.LENGTH_SHORT).show());
                return;
            }

            boolean[] checkedItems = new boolean[factoryUsers.size()];

            ((Activity) context).runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select factories to send BOQ")
                        .setMultiChoiceItems(factoryNames.toArray(new String[0]), checkedItems,
                                (dialog, which, isChecked) -> checkedItems[which] = isChecked)
                        .setPositiveButton("Send", (dialog, which) -> {
                            List<String> selectedFactoryIds = new ArrayList<>();
                            for (int i = 0; i < checkedItems.length; i++) {
                                if (checkedItems[i]) {
                                    selectedFactoryIds.add(factoryUsers.get(i).getId());
                                }
                            }

                            if (selectedFactoryIds.isEmpty()) {
                                Toast.makeText(context, "Please select at least one factory", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            SendBOQRequest request = new SendBOQRequest(project.getProjectId(), selectedFactoryIds);
                            ProjectManager projectManager = new ProjectManager();

                            projectManager.sendBOQToFactories(request).thenRun(() -> {
                                ((Activity) context).runOnUiThread(() -> {
                                    Toast.makeText(context, "BOQ sent successfully", Toast.LENGTH_SHORT).show();
                                    if (onSuccess != null) onSuccess.run();
                                });
                            }).exceptionally(e -> {
                                ((Activity) context).runOnUiThread(() ->
                                        Toast.makeText(context, "Failed to send BOQ", Toast.LENGTH_SHORT).show());
                                e.printStackTrace();
                                return null;
                            });

                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

        }).exceptionally(e -> {
            ((Activity) context).runOnUiThread(() ->
                    Toast.makeText(context, "Failed to load factories", Toast.LENGTH_SHORT).show());
            e.printStackTrace();
            return null;
        });
    }
}
