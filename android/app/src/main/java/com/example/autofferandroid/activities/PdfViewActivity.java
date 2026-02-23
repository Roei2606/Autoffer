package com.example.autofferandroid.activities;

import android.graphics.Canvas;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.autofferandroid.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfViewActivity extends AppCompatActivity {

    public static final String EXTRA_PDF_NAME = "pdfFileName";
    public static final String EXTRA_PDF_BYTES = "pdfBytes";

    private LinearLayout pdfContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        pdfContainer = findViewById(R.id.pdfContainer);

        String fileName = getIntent().getStringExtra(EXTRA_PDF_NAME);
        List<Byte> byteList = (List<Byte>) getIntent().getSerializableExtra(EXTRA_PDF_BYTES);

        if (byteList == null || byteList.isEmpty()) {
            Toast.makeText(this, "No PDF data provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            File tempPdf = new File(getCacheDir(), fileName);
            try (FileOutputStream fos = new FileOutputStream(tempPdf)) {
                byte[] byteArray = new byte[byteList.size()];
                for (int i = 0; i < byteList.size(); i++) {
                    byteArray[i] = byteList.get(i);
                }
                fos.write(byteArray);
            }

            displayPdf(tempPdf);
        } catch (IOException e) {
            Log.e("PdfViewActivity", "Failed to write PDF file", e);
            Toast.makeText(this, "Error displaying PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayPdf(File pdfFile) {
        try (ParcelFileDescriptor pfd = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
             PdfRenderer renderer = new PdfRenderer(pfd)) {

            int zoom = 2; // ✅ Zoom factor (adjust to taste)

            for (int i = 0; i < renderer.getPageCount(); i++) {
                PdfRenderer.Page page = renderer.openPage(i);

                int width = page.getWidth() * zoom;
                int height = page.getHeight() * zoom;

                android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(
                        width, height, android.graphics.Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bitmap);
                canvas.scale(zoom, zoom);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                imageView.setImageBitmap(bitmap);
                pdfContainer.addView(imageView);

                page.close();
            }

        } catch (IOException e) {
            Log.e("PdfViewActivity", "Error rendering PDF", e);
            Toast.makeText(this, "Failed to render PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
