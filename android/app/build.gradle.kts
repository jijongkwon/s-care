import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.chaquo.python")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin") // Hilt 플러그인 추가
}

android {
    namespace = "com.scare"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.scare"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CLIENT_ID", "\"${getApiKey("CLIENT_ID")}\"")
        buildConfigField("String", "BASE_URL", "\"${getApiKey("BASE_URL")}\"")

        resValue("string", "map_api_key", getApiKey("MAP_API_KEY"))

        vectorDrawables {
            useSupportLibrary = true
        }

        ndk {
            // On Apple silicon, you can omit x86_64.
            abiFilters += listOf("arm64-v8a", "x86_64")
        }

        flavorDimensions += "pyVersion"
        productFlavors {
            create("py310") { dimension = "pyVersion" }
        }

        // API 키 설정 추가
        buildConfigField("String", "WEATHER_API_KEY", "\"${getApiKey("WEATHER_API_KEY")}\"")
        resValue("string", "map_api_key", getApiKey("MAP_API_KEY"))
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

fun getApiKey(key: String): String {
    return gradleLocalProperties(rootDir).getProperty(key) ?: ""
}

chaquopy {
    defaultConfig {
        version = "3.10"

        pip {
            install("src/main/python/numpy-1.23.3-0-cp310-cp310-android_21_arm64_v8a.whl")
            install("scipy")
        }
    }
    productFlavors {
        getByName("py310") { version = "3.10" }
    }
    sourceSets {
        getByName("main") {
            srcDir("src/main/python")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.coil.compose.v200)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.work.runtime.ktx)

    //Naver Map
    implementation(libs.map.sdk)
    implementation(libs.naver.map.compose)
    implementation(libs.play.services.location)
    implementation(libs.naver.map.location)

    implementation(platform(libs.firebase.bom.v3222)) // Firebase BOM을 사용하여 버전 관리
    implementation(libs.firebase.auth.ktx) // Kotlin 확장 프로그램 사용
    implementation(libs.play.services.auth)
    implementation(libs.androidx.runtime.livedata)

    //gif
    implementation(libs.coil.compose.v240)
    implementation(libs.coil.gif)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson) // Gson 변환기
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)

    implementation(libs.accompanist.permissions)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor) // 로깅 인터셉터 (디버깅용)
    implementation(libs.okhttp.v493) // 최신 버전으로 대체 가능
    implementation(libs.okhttp.urlconnection)


    //dataStore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.firebase.messaging.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // wearable service
    implementation(libs.gms.play.services.wearable)

    // Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

}