import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.build.konfig)
    alias(libs.plugins.mokkery)
}

buildkonfig {
    packageName = "com.revest.demo.shared.data"
    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "", "")
    }
}
kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "shared-data"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            api(libs.ktor.client.cio)
        }
        commonMain.dependencies {
            implementation(project(":shared-domain"))

            implementation(libs.kotlin.corutiens.core)

            api(libs.bundles.ktor.data)
            api(libs.multiplatform.settings.no.arg)
        }
    }
}

android {
    namespace = "com.revest.demo.shared.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
}
