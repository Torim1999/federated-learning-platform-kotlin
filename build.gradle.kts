
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    application
}

group = "com.torim1999.fl"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    // Add other dependencies for federated learning, e.g., gRPC, serialization
    // implementation("io.grpc:grpc-netty-shaded:1.59.0")
    // implementation("io.grpc:grpc-protobuf:1.59.0")
    // implementation("io.grpc:grpc-stub:1.59.0")
    // implementation("com.google.protobuf:protobuf-java:3.24.4")
}

application {
    mainClass.set("com.torim1999.fl.FederatedClientKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
