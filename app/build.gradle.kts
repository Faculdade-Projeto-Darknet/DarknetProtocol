plugins {

    id("com.android.application")
    id("com.google.gms.google-services")

}

android {

    namespace = "com.darknetprotocol"

    compileSdk = 35

    defaultConfig {

        applicationId = "com.darknetprotocol"

        minSdk = 24
        targetSdk = 35

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_17

        targetCompatibility =
            JavaVersion.VERSION_17
    }
}

dependencies {

    implementation("com.google.firebase:firebase-firestore")

    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("com.google.android.material:material:1.12.0")

    implementation(
        platform(
            "com.google.firebase:firebase-bom:33.5.1"
        )
    )

    implementation(
        "com.google.firebase:firebase-auth"
    )

    implementation(
        "com.google.android.gms:play-services-auth:21.2.0"
    )

}