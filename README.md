# Autoffer Android App

Autoffer is a **next-generation Android application** for managing aluminum construction projects and quotes (BOQ/Quote).  
It revolutionizes how **Private Customers** request offers, how **Factories** respond with detailed quotes, and soon will provide **Architects** with advanced project templates and integrations.

The app is built on a **modular SDK architecture** and uses **Fragments** for flexible navigation.  
Each major service (Projects, Chat, Users, RSocket, etc.) has its **own dedicated SDK**, ensuring clarity, maintainability, and performance.

---

## 🌟 Key Advantages

### For Private Customers
- **Guided Project Creation**: Add products either manually (just width & height) or via photo/scan processed by our Window-Measurement microservice.
- **Smart Recommendations**: Automatic matching of aluminum profiles and glass types with cost indicators ($/$$).
- **Time Savings**: Adding an item takes ~15 seconds instead of 2–5 minutes in legacy systems.
- **Transparency**: Receive BOQ PDF automatically, send to multiple factories, and compare quotes in one place.
- **Seamless Communication**: Built-in chat with factories, including BOQ and Quote PDFs.

### For Factories
- **Structured BOQ PDFs**: Receive clear, standardized BOQ documents with all dimensions and details.
- **DocAI Integration**: Process original BOQ documents into structured data within seconds (instead of hours).
- **Quote Automation**: Quickly generate Quote PDFs with logos, default notes, VAT, and signature fields.
- **Reduced Errors**: Less manual data entry, fewer mistakes, and more consistent quotes.
- **Efficiency**: Save hours of repetitive work per project, respond faster to clients, and improve win rates.

### For Architects (Future Roadmap)
- **User Type Supported**: Architects can already register and use the app as a customer.
- **Planned Features**: Project templates, CAD/BIM integration, and multi-client project management.

---

## 🏗 Architecture

- **Fragments-based navigation** inside `MainActivity`.
- **Reactive networking** using **RSocket over WebSocket** (real-time chat, BOQ/Quote messages).
- **HTTP/REST** for AI microservices:
  - `/measure` → Window-Measurement microservice for image-based dimension extraction.
  - `/autoQuote` → DocAI service for parsing BOQ PDFs into structured quotes.
- **Local storage** with Room (`LocalProjectSDK`), enabling offline project creation.
- **Server**: Kotlin (Spring Boot), MongoDB, RSocket TCP (7001), Gateway (8090), DocAI (5051), and Window-Measurement service (8001).

---


---

## 🧩 Dedicated SDK per Service

One of Autoffer's unique strengths is that the Android client is organized into **dedicated SDK modules**,  
with each SDK responsible for a single service. This modular approach ensures:

- **Separation of concerns** → Clear boundaries between features.  
- **Reusability** → SDKs can be integrated in other clients (iOS, Web).  
- **Performance** → DTOs are kept lightweight for faster transfers.  
- **Maintainability** → Each SDK can evolve independently.  

### SDKs Overview
- `chatSDK` → Messaging, BOQ/Quote messages, file sharing.  
- `projectsSDK` → Project creation, saving, retrieving BOQ/Quote PDFs.  
- `usersSDK` → User management and sessions.  
- `rsocketSDK` → RSocket networking client.  
- `localProjectSDK` → Local Room storage of unsaved projects.  
- `pricingSDK` → Lightweight cost indicators ($/$$).  
- `adsSDK` → Advertisement models and requests.  
- `coremodelsSDK` → Shared DTOs across modules.  

Each SDK is written in **Java** for Android and communicates with the backend via **RSocket** or **HTTP**, depending on the service.

---

## 📦 SDK Modules (with examples)

Each service has its **own SDK**, ensuring clean code and faster data transfers.

### 1. `chatSDK`
Handles messaging and file transfer between clients and factories.

```java
Message msg = new Message(chatId, senderId, receiverId, "Hello", "TEXT");
Message sent = messageManager.sendMessage(msg);
```

### 2. `projectsSDK`
Manages project creation and retrieval of BOQ/Quote PDFs.

```java
CreateProjectRequest req = new CreateProjectRequest(clientId, "123 Main St", items, factoryIds);
ProjectDTO project = projectManager.createProject(req);
```

### 3. `usersSDK`
Responsible for user session, profile type, and contact info.

```java
User currentUser = userManager.getCurrentUser();
String profileType = currentUser.getProfileType(); // PRIVATE_CUSTOMER / FACTORY / ARCHITECT
```

### 4. `rsocketSDK`
Provides RSocket client for request-response and streaming.

```java
ProjectDTO response = rSocketClient
    .requestResponse("projects.create", req, ProjectDTO.class);
```

### 5. `localProjectSDK`
Stores project items locally until the project is saved.

```java
localDao.insert(new LocalItemEntity(profileId, glassId, width, height, quantity, position));
```

### 6. `pricingSDK`
Calculates lightweight cost indicators.

```java
String indicator = pricingHelper.getPriceIndicator(item);
```

### 7. `adsSDK`
Handles ad models and requests (optional feature).

```java
AdsRequest request = new AdsRequest("banner");
```

### 8. `coremodelsSDK`
Contains all DTOs shared across SDKs.

```java
ItemModelDTO item = new ItemModelDTO(profileId, glassId, 120, 150, 2, "Living Room");
```

---

## 👥 User Roles

- **Private Customer**  
  Create projects quickly, send BOQ PDFs, and compare multiple factory quotes seamlessly.

- **Factory**  
  Receive BOQ PDFs, generate structured Quote PDFs, and communicate directly with clients.

- **Architect (Future)**  
  Currently only profile creation is available. Planned features include templates, CAD/BIM integration, and advanced project management.

---

## 📱 App Screens & UX Flow

The Autoffer Android app is composed of **Fragments** (except where noted), ensuring modular navigation.  
Below is a complete list of the main screens with their purpose.

| Screen / Fragment            | Purpose |
|-------------------------------|---------|
| **Splash / Launch**           | Displays the Autoffer logo briefly before navigation starts. |
| **Login Fragment**            | User login with email & password. Authentication is handled by the server (MongoDB). |
| **Register Fragment**         | User registration. Includes full details (first/last name, email, password, phone, address, profile type: PRIVATE_CUSTOMER / FACTORY / ARCHITECT). |
| **Forgot Password Fragment**  | Password reset handled by the backend (server logic). |
| **MainActivity (Host)**       | Hosts the navigation graph and all Fragments. |
| **New Project Fragment**      | Start a new project by entering project address and choosing method (manual entry or image capture). |
| **Add Manual Fragment**       | Enter width & height, select aluminum profile and glass type, define quantity & position, then add to the project. |
| **Image Capture / Picker**    | Capture a photo or pick from gallery. The image is processed by the Window-Measurement microservice to extract dimensions. |
| **Current Project Fragment**  | Displays all locally added items in a RecyclerView. Shows indicators ($/$$). Includes “Add Another Product” and “Save Project” buttons. |
| **My Projects Fragment**      | Displays all saved projects. Uses a dynamic `ProjectAdapter`: one layout for Private Customers, another for Factories. |
| **Edit Project Fragment**     | Allows editing only the **quantity** and **position** of each item. |
| **User Directory Fragment**   | Lists factories/architects depending on current user type. Includes “Show Contact Info” and “Open Chat” buttons. |
| **Chat Activity**             | Real-time chat (RSocket). Supports text, BOQ messages, Quote messages, file transfers, and action buttons (Accept/Reject/View). |
| **Pdf View Activity**         | Opens and displays PDF files (BOQ or Quote). |
| **Factory View (in Adapter)** | Special view mode inside MyProjects for factories: allows sending Quotes, showing status updates, and confirming sent offers. |

---


---

## 📷 Image Measurement Service

Autoffer integrates a dedicated **Window-Measurement microservice**.  
This service allows the user to **take a photo of an empty wall opening or an existing aluminum window**.  
The system processes the image and automatically extracts **accurate dimensions**. Based on these dimensions, the app suggests suitable aluminum profiles and glass types.

### Manual Alternative
If preferred, the process can be performed manually: the user simply enters **width** and **height**, and the system provides the same smart recommendations for matching products.

---

## ⏱ Efficiency Gains

- **Private Customers**:  
  - Item creation in ~15s vs 2–5 minutes manually.  
  - Entire project in minutes instead of hours.  

- **Factories**:  
  - BOQ with 13 items: seconds per page vs ~1 hour manually.  
  - Drastically reduced errors and faster turnaround times.  

- **Overall Impact**:  
  - Quotes delivered in **minutes** instead of **hours/days**.  
  - Centralized communication reduces friction and misunderstandings.  

---

## ⚙️ Networking

- **RSocket/WebSocket** → Real-time chat, BOQ/Quote workflows.  
- **HTTP REST** → Microservices (e.g., `POST /measure`).  
- **Local Room DB** → Temporary storage until project is saved.  

---

