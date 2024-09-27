
val ftcVersion = "10.1.0"
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ca.helios5009.hyperion"
    compileSdk = 33
    ndkVersion = "27.1.12297006"

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    compileOnly("org.firstinspires.ftc:RobotCore:$ftcVersion")
    compileOnly("org.firstinspires.ftc:Hardware:$ftcVersion")
    compileOnly("org.firstinspires.ftc:FtcCommon:$ftcVersion")
    compileOnly("org.firstinspires.ftc:Vision:$ftcVersion")
    implementation("androidx.core:core-ktx:1.5.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.6.10"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}