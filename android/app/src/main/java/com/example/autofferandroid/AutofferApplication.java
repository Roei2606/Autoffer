package com.example.autofferandroid;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.rsocket_sdk.network.RSocketClientManager;

public class AutofferApplication extends Application {

    private static final String TAG = "AutofferApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        boolean connected = RSocketClientManager.getInstance().connect();

        if (connected) {
            Log.i(TAG, "✅ Connected to server. Sending ping...");
        } else {
            Log.e(TAG, "❌ Failed to connect to RSocket server.");
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(getApplicationContext(), "❌ RSocket connection failed", Toast.LENGTH_LONG).show()
            );
        }
    }
}
