package com.example.autofferandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.autofferandroid.activities.PdfViewActivity;
import com.example.chat_sdk.models.Message;
import com.example.projects_sdk.network.ProjectManager;

import java.util.ArrayList;

public class QuoteMessageHandler {

    public static void openQuotePdf(Context context, Message message) {
        try {
            String projectId = extractProjectIdFromFileName(message.getFileName());
            String factoryId = message.getSenderId(); // המפעל ששלח את ההצעה

            ProjectManager manager = new ProjectManager();
            manager.getQuotePdf(projectId, factoryId).thenAccept(pdfBytes -> {
                Intent intent = new Intent(context, PdfViewActivity.class);
                intent.putExtra(PdfViewActivity.EXTRA_PDF_NAME, message.getFileName());
                intent.putExtra(PdfViewActivity.EXTRA_PDF_BYTES, new ArrayList<>(pdfBytes));
                context.startActivity(intent);
            }).exceptionally(error -> {
                Log.e("QuoteMessageHandler", "❌ Failed to fetch quote PDF", error);
                showToast(context, "Failed to load quote PDF");
                return null;
            });

        } catch (Exception e) {
            Log.e("QuoteMessageHandler", "❌ Unexpected error", e);
            showToast(context, "Something went wrong while loading the quote");
        }
    }

    private static String extractProjectIdFromFileName(String fileName) {
        return fileName.replace("QUOTE_Project_", "").replace(".pdf", "");
    }

    private static void showToast(Context context, String msg) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        );
    }
}
