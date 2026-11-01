# 🌿 PantryPal – Smart Pantry Management System

<p align="center">
  <img src="pantrypal-logo.png" alt="PantryPal Logo" width="130">
</p>

<h3 align="center">Your Kitchen, Intelligently Organised.</h3>

<p align="center">
  A Java-based desktop application designed to simplify pantry management, monitor food expiry dates, organise shopping lists, and suggest recipes using available ingredients.
</p>

---

## 📌 About the Project

**PantryPal** is a Smart Pantry Management System developed using Java. It helps users manage kitchen inventory, monitor stock levels, track expiry dates, and organise grocery requirements through an interactive graphical interface.

The application provides two desktop interfaces:

- **JavaFX:** A modern dashboard with a Dark Forest Green theme.
- **Java Swing:** An alternative interface for pantry and shopping list management.

Both interfaces use a shared backend and local file-based storage, allowing pantry and shopping list data to be accessed across the two interfaces when the application is reopened.

## 📑 Table of Contents

- [Key Features](#-key-features)
- [Technologies Used](#️-technologies-used)
- [System Architecture](#️-system-architecture)
- [Project Structure](#-project-structure)
- [Application Screenshots](#️-application-screenshots)
- [Getting Started](#️-getting-started)
- [Testing](#-testing)
- [Current Limitations](#️-current-limitations)
- [Future Enhancements](#-future-enhancements)
- [Project Information](#-project-information)

---

## ✨ Key Features

- **Dashboard:** Provides an overview of pantry inventory, stock levels and expiry-related information.
- **Pantry Management:** Add, update, delete and search pantry items while maintaining details such as name, category, quantity, unit and expiry date.
- **Stock Monitoring:** Identifies low-stock and out-of-stock items based on their minimum stock levels.
- **Expiry Tracking:** Identifies expired and soon-to-expire ingredients to help users manage groceries effectively.
- **Smart Shopping List:** Add and remove shopping items, mark purchases as completed, and automatically add eligible low-stock and out-of-stock pantry items.
- **Recipe Suggestions:** Suggests predefined recipes based on available pantry ingredients. Recipes requiring unavailable or expired ingredients are excluded.
- **Data Persistence:** Stores pantry inventory and shopping list information in local text files, preserving data between application sessions.
- **Dual Desktop Interfaces:** Provides JavaFX and Java Swing interfaces connected to shared backend functionality.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java (JDK 26) | Core application development |
| JavaFX 27.0.1 | Primary graphical interface |
| Java Swing | Alternative graphical interface |
| FXML | JavaFX layout design |
| CSS | JavaFX interface styling |
| Java File I/O | Local data storage and retrieval |
| ArrayList & Collections | Data management |
| LocalDate | Expiry date handling |
| Git & GitHub | Version control |
| Visual Studio Code | Development environment |

## 🏗️ System Architecture

PantryPal follows a modular architecture that separates the user interfaces, business logic and data storage.

```text
                     PANTRYPAL
                         |
              -----------------------
              |                     |
           JavaFX                 Swing
              |                     |
              -----------------------
                         |
                   Shared Backend
                         |
          --------------------------------
          |              |               |
     PantryManager  ShoppingListManager  RecipeSuggestion
          |              |               Service
          --------------------------------
                         |
                     FileManager
                         |
              ---------------------
              |                   |
          pantry.txt       shopping_list.txt
📂 Project Structure
PantryPal/
│
├── Main.java
├── PantryItem.java
├── ShoppingListItem.java
├── PantryManager.java
├── ShoppingListManager.java
├── FileManager.java
├── RecipeSuggestionService.java
│
├── SwingMain.java
├── JavaFXMain.java
├── DashboardController.java
├── dashboard.fxml
├── styles.css
│
├── pantrypal-logo.png
├── pantry.txt
├── shopping_list.txt
│
├── README.md
│
└── screenshots/
    ├── dashboard.png
    ├── pantry.png
    ├── shopping-list.png
    ├── recipes.png
    └── swing-interface.png
🖥️ Application Screenshots

The following screenshots showcase the application's desktop interfaces and major functionalities.

JavaFX Dashboard

Pantry Management

Shopping List

Recipe Suggestions

Java Swing Interface

⚙️ Getting Started
Prerequisites

Ensure that the following are installed:

Java Development Kit (JDK 26)
JavaFX SDK 27.0.1
Visual Studio Code or IntelliJ IDEA
Git
1. Clone the Repository
git clone https://github.com/sathviktrd76/PantryPal.git

Navigate to the project directory:

cd PantryPal
2. Configure JavaFX

Download and extract JavaFX SDK 27.0.1.

Locate the lib directory inside the extracted JavaFX SDK.

3. Compile the JavaFX Application

Replace PATH_TO_JAVAFX_LIB with the actual path to your JavaFX SDK's lib directory.

javac --module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml *.java
4. Run the JavaFX Application
java --module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml JavaFXMain
5. Run the Java Swing Application
java SwingMain
🧪 Testing

The application was tested to verify the functionality of its major components.

Testing included:

Pantry item addition, updating, deletion and searching.
Correct identification of low-stock and out-of-stock items.
Expiry-date monitoring and status classification.
Shopping list addition and removal.
Marking shopping list items as purchased.
Automatic addition of eligible low-stock items to the shopping list.
Prevention of duplicate unpurchased shopping list entries.
Recipe suggestions based on available, non-expired ingredients.
Removal of recipe suggestions when required ingredients become unavailable or expire.
Data persistence across application sessions.
Sequential data sharing between JavaFX and Swing interfaces.
⚠️ Current Limitations
Pantry and shopping list information is stored locally using text files rather than a database.
Data sharing between JavaFX and Swing relies on common files. Changes are reflected when the other interface reloads its data; real-time simultaneous synchronisation is not supported.
Recipe suggestions are based on predefined rules and ingredient availability rather than machine learning or AI-based recommendations.
The application currently supports desktop interfaces only.
The application does not currently provide cloud backup or online synchronisation.
🔮 Future Enhancements

The following features are potential improvements for future versions of PantryPal:

Database Integration: Replace local text-file storage with a database for structured data management.
Barcode Scanning: Simplify pantry item entry through barcode-based product identification.
Expiry Notifications: Introduce timely reminders for products approaching their expiry dates.
Advanced Recipe Recommendations: Expand recipe suggestions with nutritional information and personalised recommendations.
Cloud Backup and Synchronisation: Enable secure data storage and access across multiple devices.
Online Grocery Integration: Explore integration with online grocery platforms.
Mobile Application: Extend pantry management functionality to smartphones and tablets.
Enhanced Analytics: Introduce visual insights into consumption patterns, purchasing habits and food waste.
👨‍💻 Project Information
Detail	Description
Project Name	PantryPal – Smart Pantry Management System
Project Domain	Java Application Development
Programming Language	Java
Application Type	Desktop Application
University	CMR University
Programme	B.Tech – Computer Science and Engineering (AI & ML)
👨‍💻 Made By

Sathvik Talabathula

B.Tech – Computer Science and Engineering (AI & ML)
CMR University

GitHub: @sathviktrd76

<p align="center"> <strong>PantryPal – Your Kitchen, Intelligently Organised. 🌿</strong> </p> ```
