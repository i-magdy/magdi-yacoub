plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.protobuf)
}

android {
    namespace = "org.myf.demo.core.datastore"
    compileSdk = 34
    defaultConfig {
        minSdk = 22

        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions{
        sourceCompatibility =  JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions{
        jvmTarget = "1.8"
    }


}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.3"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }

            }
        }
    }
}





dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(libs.androidx.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test)
    androidTestImplementation(libs.androidx.espresso)
    implementation(libs.protobuf.lite)
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.core)
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)
    kapt(libs.androidx.lifecycle.compiler)
}


kapt {
    correctErrorTypes = true
}