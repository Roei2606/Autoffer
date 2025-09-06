package com.example.rsocket_sdk.network;

/** כתובות קבועות — עדכן את LAN_IP אם תרצה לבדוק דרך ה-Wi-Fi */
public final class ServerConstants {
    private ServerConstants() {}

    /** אמולטור → המחשב המארח */
    public static final String EMULATOR = "ws://10.0.2.2:8080/rsocket";

    /** מכשיר אמיתי + ADB reverse: adb reverse tcp:8080 tcp:8080 */
    public static final String LOOPBACK = "ws://127.0.0.1:8080/rsocket";

    /** מכשיר אמיתי על אותה רשת Wi-Fi (רשות) — עדכן ל-IP של המחשב שלך */
    public static final String LAN = "ws://192.168.1.137:8080/rsocket";

    /** ענן (TLS) — תמיד זמין */
    public static final String PUBLIC = "wss://autoffer-server-313683195324.europe-west1.run.app/rsocket";
}