package com.example.rsocket_sdk.network;

import android.os.Build;

import com.example.core_models_sdk.BuildConfig;

import java.util.ArrayList;
import java.util.List;

public final class ServerConfig {
    private ServerConfig() {}

    /** האם רץ על אמולטור */
    public static boolean isEmulator() {
        String fp = Build.FINGERPRINT, model = Build.MODEL, brand = Build.BRAND, device = Build.DEVICE;
        return (fp != null && (fp.startsWith("generic") || fp.startsWith("unknown"))) ||
                (model != null && (model.contains("google_sdk") || model.contains("Emulator") || model.contains("Android SDK built for x86"))) ||
                (brand != null && brand.startsWith("generic") && device != null && device.startsWith("generic")) ||
                "google_sdk".equals(device);
    }

    /** רשימת מועמדים לפי מצב: דיבאג/ריליס + אמולטור/מכשיר */
    public static List<String> getCandidateUrls() {
        List<String> list = new ArrayList<>();
        if (BuildConfig.DEBUG) {
            if (isEmulator()) {
                list.add(ServerConstants.EMULATOR); // 10.0.2.2
                list.add(ServerConstants.PUBLIC);   // רזרבה תמידית
            } else {
                list.add(ServerConstants.LOOPBACK); // עובד עם adb reverse
                list.add(ServerConstants.LAN);      // Wi-Fi (עדכן IP)
                list.add(ServerConstants.PUBLIC);   // רזרבה
            }
        } else {
            list.add(ServerConstants.PUBLIC);       // בפרודקשן – רק ענן
        }
        return list;
    }

    /** תאימות לאחור – מחזיר הראשון */
    public static String getDefaultUrl() {
        List<String> cs = getCandidateUrls();
        return cs.isEmpty() ? ServerConstants.PUBLIC : cs.get(0);
    }
}