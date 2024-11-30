# GitHub Search App

**GitHub Search** is a mobile application built using **Jetpack Compose** to search and explore GitHub repositories via the **GitHub API**. Users can search for repositories, view details of a selected repository, and browse contributors. The app leverages modern Android development practices and libraries, including **MVVM**, **Coroutines**, **Room**, and **Jetpack Libraries**, ensuring a robust and seamless user experience.

---

## ✨ Features

### Home Screen
- **Search Bar**: Enables users to search repositories using the GitHub API.
- **Search Results**:
  - Displays search results using a **LazyColumn** styled with card views.
  - Shows a maximum of 10 items per page with **pagination** to load additional results.
- **Offline Access**:
  - The first 15 search results are saved locally using Room for offline access.
- **Navigation**:
  - Tapping on a repository navigates to the **Repo Details Screen**.

### Repo Details Screen
- Displays detailed information about the selected repository, including:
  - **Image**: Repository owner's avatar.
  - **Name**: Repository name.
  - **Project Link**: A clickable link to the repository, opening in a WebView.
  - **Description**: Repository description.
  - **Contributors**: List of contributors.
- **WebView Integration**: Open the project link directly within the app.

---
## 📹 App Demo



https://github.com/user-attachments/assets/4cf4d4e7-64e4-40ec-9c77-3dba4000b659



---

## 🛠️ Tech Stack

- **Programming Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Concurrency**: Coroutines
- **Database**: Room
- **Networking**: Retrofit with Gson
- **Data Flow**: StateFlow
- **Repository Pattern**: Ensures clean and modular code.

---

## 📐 App Architecture

- **MVVM Pattern**: To separate UI, data handling, and business logic.
- **Repository Layer**: Manages data flow from both network and local database.
- **Jetpack Components**: Utilizes ViewModel, StateFlow, and Room for data persistence and lifecycle-aware operations.

---

## 🚀 Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/github-search.git
   cd github-search
   ```
  
2. Build and run the app:
   - Open the project in Android Studio.
   - Sync Gradle and run the app on an emulator or device.

---

## 🧩 Additional Features

- **Pagination**: Load more repositories as users scroll through the search results.
- **Offline Access**: Ensures seamless browsing even without an internet connection.
- **Modern UI**: Built with Jetpack Compose for a clean and responsive interface.

---

## 📝 To-Do

1. **Improve Color Scheme and Theme**:
   - Enhance the app's visual appeal by refining the color palette and theming for a more cohesive user experience.

2. **Add Dependency Injection (DI)**:
   - Implement a DI framework such as **Hilt** or **Koin** for better dependency management.

---

## 👩‍💻 Contributions

Contributions are welcome! Feel free to fork the repository and submit pull requests.
