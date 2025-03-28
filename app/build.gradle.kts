plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.gyarsilalsolanki011.bankingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gyarsilalsolanki011.bankingapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit Core Library
    implementation(libs.retrofit);

    // GSON Converter for JSON Parsing
    implementation(libs.json.converter)

    // OkHttp for Logging Network Requests (Optional but Recommended)
    implementation(libs.logging.interceptor)

    /*
    // ViewPager2 (Required for swiping pages)
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    // Dots Indicator (For showing page indicators)
    implementation ("com.tbuonomo:dotsindicator:4.3")
    */
}