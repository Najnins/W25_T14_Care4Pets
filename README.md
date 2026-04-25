# 🐾 Care4Pets 
A native Android application for managing your pet's health records, reminders, care instructions, and appointment reminders — all stored privately on your device.

---
## 📌 Overview 
Care4Pets provides an easy way for pet owners to track vaccinations, medical records, daily reminders, and general pet care activities.  
The goal of this project is to deliver improved UI, screen navigation, and basic front-end logic.

---

## 🛠️ Technologies Used  
- **Android Studio (JAVA + XML)**  
- **Material Design Components**  
- **Dummy in-app data (no backend)**  

---

## ✔️ Prototype 2 Requirements (Completed)

### **1. Improved UI**  
- Updated onboarding screens  
- Enhanced color gradients & icon styling  
- Updated login screen (spacing, icons, text styles)

### **2. Logic Implemented on 80% of Screens**  
- Onboarding → Next screen navigation  
- Skip → Login screen  
- Login → Dashboard (dummy screen)  
- Basic form validation (Toast messages)

### **3. Data Access Layer (Dummy Data)**  
- Pets list screen uses static/dummy data  
- Reminders screen populated with temporary list items  
- Health Records & Care Instructions use static content

---

## 📱 App Screens (Prototype 2)

### ⭐ Onboarding Screens  
- Track Pet Health  
- Appointment Reminders  
- Navigation: Next / Skip buttons  

### ⭐ Login Screen  
- Email + password  
- Forgot password (placeholder)  
- Create account (placeholder)  

### ⭐ Dashboard (Prototype)  
- Buttons for Pets, Reminders, Health Records, Settings  

### ⭐ My Pets Screen  
- Static data list of pets  
- Basic card UI  

### ⭐ Reminders Screen  
- Dummy reminder entries  

### ⭐ Health Screens  
- Static medical records page  
- Care instructions list  

---

## 👩‍💻 Team Responsibilities

### **Najnin – Onboarding + Splash + navigation flow**
Make splash → onboarding → login work smoothly.
Store a flag “first time user?” 


### **Arad – Login + Signup**
Dummy credentials or simple “local user” model.
On successful login → go to Dashboard.


### **Shaheer – Dashboard + Pets list**
Dashboard cards navigate correctly.
PetsList shows data from Repository.


### **Niaj – Pet profile + Reminders**
Show selected pet details.
Simple list of health records.


### **Brian – Health Records + Care instructions**
Ability to add a new health record.
Simple list of health records.
Dynamic care tips text screen.
Ability to add new Care Instructions.

---

## Getting Started

### 1. Download the Zip from GitHub

### 2. Open the Project in Android Studio

1. Launch **Android Studio**
2. Click **File -> Open**
3. Navigate to the folder and select the root `Group14-Capstone-Project-2` folder
4. Wait for Gradle to sync.

## Running the App
There are two ways to run this app:

1. Use the Android Studio Emulator
or
2. Use Android Phone connected via USB debugging enabled.

## Creating An Account

Care4Pets requires registration before you can use the app. There are no pre-built test accounts - you must create your own.

## How to Register

1. Launch the app and tap through the onboarding screens.
2. Tap **Create Account** on the login screen.
3. Fill out the form.
4. Password must be minimum 8 characters, have a number, a special character and an uppercase letter.

## Authors
**Group 14 — Capstone Project**
 
| Name | Role |
|---|---|
| Brian Pagsolingan | Backend Developer and Frontend Developer |
| Shaheer Ansari | Quality Assurance Tester |
| Najnin Sultana | Owns the Repository and Backend Developer|
| Arad Tahmasebifar |UX Designer and Frontend Developer |
| Niaj Hossain | Frontend Developer |
 
