# Project Report: Namma-Shaale Inventory Management System

## 1. Introduction
Namma-Shaale is an Android-based Inventory Management application designed for government schools to track and manage physical assets like Lab equipment, Sports gear, and ICT tools.

## 2. Problem Statement
Manual record-keeping in schools leads to errors, loss of accountability, and difficulty in tracking the condition (Working/Broken) of school property over time.

## 3. Technology Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose (Declarative UI)
- **Database:** Room (SQLite abstraction)
- **Architecture:** MVVM (Model-View-ViewModel)

## 4. Implementation details
- **Frontend:** Built using Composable functions. The `DashboardScreen` uses a `LazyColumn` for performance and `Modifier.blur()` for visual hierarchy.
- **Backend/Database:** Room database handles persistence.
- **Logging:** The app uses an `Issue` entity to log all activities (Asset added, status changed, etc.), which are displayed in the Recent Activity feed.

## 5. Important Files for Viva
- **UI Logic:** `MainScreen.kt`, `DashboardScreen.kt`
- **Business Logic:** `InventoryViewModel.kt`
- **Database Setup:** `Entities.kt`, `InventoryDao.kt`, `InventoryDatabase.kt`
