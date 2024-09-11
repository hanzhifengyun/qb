plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.jetbrains.kotlin.kapt)
}

android {
    namespace = "com.hzfy.common"
    compileSdk = 34
    resourcePrefix ("common_")

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }


    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    hilt {
        enableAggregatingTask = false
    }

}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    api(libs.permission.flow.android)
    api(libs.permission.flow.compose)

    api(project(":library"))
    api(project(":ui"))
    api(project(":database"))

    api(libs.hilt.android)
    kapt(libs.hilt.compiler)
}