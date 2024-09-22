plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 35

    defaultConfig {
        applicationId = "com.joeblakeb.battleships"
        minSdk = 16
        targetSdk = 35
        versionCode = 2
        versionName = "1.1"

        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles.add(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFiles.add(file("proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "com.joeblakeb.battleships"
}

dependencies {
    api(project(":lib"))
    implementation(project(":logic"))
    api("io.github.pdvrieze.matrixlib:matrixlib:1.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("org.junit.jupiter:junit-jupiter:${rootProject.extra["junit_version"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${rootProject.extra["junit_version"]}")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}