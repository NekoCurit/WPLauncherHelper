plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)

    `maven-publish`
}

group = "net.nekocurit.wplauncher_helper"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    linuxX64()
    mingwX64()
    macosArm64()

    sourceSets {
        commonMain.dependencies {
            api(libs.korlibs.crypto)

            api(libs.ktor.client.core)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.serialization.kotlinx.json)
        }
        jvmMain.dependencies {
            api(libs.ktor.client.java)
        }
        nativeMain.dependencies {
            api(libs.ktor.client.cio)
        }
    }
}