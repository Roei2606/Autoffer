# Autoffer iOS App

## 📖 Overview
Autoffer is an innovative mobile application designed for the aluminum and construction industry. Its main goal is to streamline the process of generating Bill of Quantities (BOQ) and project quotations for private customers, architects, and factories.

The iOS application, like the Android version, supports project management, chat functionality, and BOQ/Quote workflows. However, several adjustments and unique requirements were necessary for the iOS platform.

---

## 🛠️ Technologies and Architecture
- **Language & Frameworks:** Swift (UIKit & programmatic UI, with some Storyboard usage in earlier stages).
- **Minimum iOS Support:** iOS 16+.
- **Firebase Integration:** Authentication, Firestore, and Storage (used for user management, profile images, and verification).
- **Networking:** Communication with the Kotlin Spring Boot backend through RSocket and WebSocket, mediated by a dedicated **Gateway microservice** to support iOS connectivity.
- **SDKs:** The iOS client mirrors the modular SDK approach from Android. Each domain (Chat, Projects, Core Models, etc.) is separated into dedicated Swift Packages for maintainability and clarity.

---

## 🔑 Differences from Android
While the business logic and backend endpoints are identical to the Android application, the iOS development required:
- **Custom Gateway Service:** Unlike Android, iOS cannot connect directly to the Kotlin RSocket server. A separate Gateway microservice was implemented to bridge communication.
- **UIKit Development:** The Android app uses Material Design Components, while iOS uses native UIKit elements and programmatic layouts to achieve a modern iOS look and feel.
- **Navigation:** Managed with `UITabBarController`, `UINavigationController`, and programmatic transitions instead of Android Fragments.
- **Platform-specific Features:**
  - Lottie animations for splash and transitions.
  - Dark Mode and Light Mode support.
  - Adapted handling of local storage, push notifications, and camera access according to iOS guidelines.

---

## 📦 Features (in progress)
- User authentication (sign up, login, password reset).
- Project creation with manual product entry or photo-based measurement.
- Chat system with file sharing (e.g., BOQ/Quote PDFs).
- Factory and architect directory with contact and chat options.
- Quote generation and status updates between customers and factories.

---

## ⚠️ Current Status
The iOS application is **still under development** and is **not yet production-ready**. Some features are partially implemented, and full integration with the backend services is in progress.
