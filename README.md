# Your Bank 🚀
A modern, feature-rich banking application for secure online and offline banking services.


## 📌 About the Project
Your Bank is an Android banking application that provides seamless online and offline banking experiences for users and admins. Built with Java, XML, and Retrofit, it ensures secure transactions and efficient account management.

## 🛠️ Tech Stack
- **Languages**: Java, XML
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: MySQL DB
- **Networking**: Retrofit, JWT Authentication
- **UI Components**: Material Design, ViewPager2, RecyclerView
- **Other Libraries**: Glide, Gson, DotsIndicator

## 🎯 Features
- 🌍 User Interface (Online Banking)
- ✅ User Authentication (Login, Registration)
- ✅ View Account Details (Savings/Current)
- ✅ Balance Visibility Toggle
- ✅ Deposit & Withdraw Money
- ✅ Transaction History (Recent transactions with relative time)
- ✅ Profile Management (Update profile & settings)
- ✅ Push Notifications (Transaction alerts)

## 🔐 Admin Interface (Offline User Management)
- ✅ Role-Based Authentication (Admin, Manager, Super Admin)
- ✅ Manage User Accounts (Create, Update, Delete)
- ✅ Monitor Transactions
- ✅ Offline Banking Support

## 📱 Screenshots
🔹 User Dashboard
(Coming Soon: Add UI screenshots here)

## 🔄 API Integration
- Your Bank uses Retrofit to call backend APIs with JWT Authentication.
```sh
@POST("/api/auth/login")
Call<LoginResponse> loginUser(
   @Query("email") String email,
   @Query("password") String password
);
```
- How to pass JWT Token?

```sh
@POST("/api/accounts/create")
    Call<StringResponse> createAccount(
            @Query("email") String email,
            @Query("accountType") String accountType,
            @Query("balance") double balance,
            @Header("Authorization") String token
    );
```
</br>

## 📥 Installation
1️⃣ Clone the repository
```sh
git clone https://github.com/gyarsilalsolanki011/Your-Bank.git
```
2️⃣ Open in Android Studio </br>
3️⃣ Run on Emulator/Physical Device </br>

## 📌 Setup API End Points with [Localhost: 8080](http://localhost:8080)
1️⃣ Change Enviromenet Variable, add this directory to path variable
```sh
C:\User_Name\hp\AppData\Local\Android\sdk\platform-tools
```
2️⃣ Now add this attribute to androidManifest
```sh
<application
        android:allowBackup="true"
        .
        .
        . 
        android:usesCleartextTraffic="true" >
```
3️⃣ Now reverse the port to 8080 using terminal
```sh
adb reverse tcp:8080 tcp:8080  
```
You can also try other adb commands to check connection with localhost. </br>

## 🚀 Future Enhancements
- Dark Mode Support
- AI-based Fraud Detection
- Loan & Investment Features
- Multi-language Support

## 📄 License
This project is open-source. Feel free to use and modify it. 😊

## 📞 Contact
For any queries, feel free to reach out: </br>
📧 Email: gyarsilalsolanki011@gmail.com </br>
🔗 LinkedIn: [Your Profile](https://linkedin.com/in/gyarsilalsolanki) </br>
🔗 GitHub API Repository: [Banking-App](https://github.com/gyarsilalsolanki011/banking-app.git)

Hope this README file meets your expectations! Let me know if you want any modifications. 🚀🔥
