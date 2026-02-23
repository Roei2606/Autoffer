package com.example.autofferandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.chat_sdk.models.Message;
import com.example.projects_sdk.models.QuoteStatus;
import com.example.projects_sdk.requests.UpdateFactoryStatusRequest;
import com.example.projects_sdk.network.ProjectManager;
import com.example.autofferandroid.activities.PdfViewActivity;
import com.example.users_sdk.network.SessionManager;

import java.util.ArrayList;

public class BOQMessageHandler {

    public static void handleApproval(Context context, Message message, boolean isApproved) {
        String projectId = extractProjectIdFromFileName(message.getFileName());
        String factoryId = SessionManager.getInstance().getCurrentUserId(); // ✅ נכון!

        QuoteStatus newStatus = isApproved ? QuoteStatus.ACCEPTED : QuoteStatus.REJECTED;

        UpdateFactoryStatusRequest request = new UpdateFactoryStatusRequest(projectId, factoryId, newStatus);
        ProjectManager manager = new ProjectManager();

        manager.respondToBoqRequest(request).thenRun(() -> {
            String msg = isApproved ?
                    "✅ Project approved. Customer has been notified." :
                    "❌ Project rejected. Customer has been notified.";
            showToast(context, msg);
        }).exceptionally(e -> {
            Log.e("BOQMessageHandler", "❌ Failed to respond to BOQ", e);
            showToast(context, "Failed to update status. Please try again.");
            return null;
        });
    }


    public static void openBoqPdf(Context context, Message message) {
        try {
            Intent intent = new Intent(context, PdfViewActivity.class);
            intent.putExtra(PdfViewActivity.EXTRA_PDF_NAME, message.getFileName());
            intent.putExtra(PdfViewActivity.EXTRA_PDF_BYTES, new ArrayList<>(message.getFileBytes()));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("BOQMessageHandler", "❌ Failed to open BOQ PDF", e);
            showToast(context, "Failed to open BOQ PDF");
        }
    }

    private static String extractProjectIdFromFileName(String fileName) {
        try {
            return fileName.replace("BOQ_Project_", "").replace(".pdf", "");
        } catch (Exception e) {
            return "";
        }
    }

    private static void showToast(Context context, String msg) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        );
    }
}
