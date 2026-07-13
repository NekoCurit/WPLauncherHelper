plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "net.nekocurit.wplauncher_helper"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    api(platform(libs.ktor.bom))
    api(libs.ktor.client.core)
    api(libs.ktor.client.java)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.serialization.kotlinx.json)
}

java {
    withSourcesJar()
}