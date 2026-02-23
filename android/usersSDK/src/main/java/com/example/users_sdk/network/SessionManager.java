package com.example.users_sdk.network;


import com.example.core_models_sdk.models.User;
import com.example.core_models_sdk.models.UserType;

public class SessionManager {

    private static SessionManager instance;
    private String currentUserId;
    private User currentUser; // ✅ הוספת המשתמש המחובר

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    // ✅ חדש – שמירה על אובייקט המשתמש כולו
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.currentUserId = user.getId(); // עדכון גם של ה-ID כדי לשמור על עקביות
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // ✅ חדש – החזרת סוג הפרופיל (לפרסומות וכו׳)
    public UserType getCurrentUserProfileType() {
        if (currentUser != null) {
            return currentUser.getProfileType();
        } else {
            throw new IllegalStateException("User is not logged in");
        }
    }

    // אופציונלי – למחוק יוזר מהזיכרון (נחוץ ל-Logout)
    public void clear() {
        currentUser = null;
        currentUserId = null;
    }
}
