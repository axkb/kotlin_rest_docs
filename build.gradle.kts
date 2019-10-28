import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val epagesRestDocsVer = "0.9.5"
    val springBootVersion = "2.2.0.RELEASE"

    repositories {
        mavenCentral()
        jcenter()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
        classpath("com.epages:restdocs-api-spec-gradle-plugin:$epagesRestDocsVer")
    }
}

apply {
    plugin("com.epages.restdocs-api-spec")
}

plugins {
    id("org.springframework.boot") version "2.1.9.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.spring") version "1.3.50"
    kotlin("plugin.jpa") version "1.3.50"
}

group = "com.docs"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
}

repositories {
    mavenCentral()
    jcenter()
}

sourceSets {
    main {
        resources {
            srcDir("src/main/resources")
        }
    }
}

dependencies {
    val junitJupiterVersion = "5.5.2"

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

//    old swagger not supports the examples open api3 blocks
//    compile("io.springfox:springfox-swagger2:2.9.2")
//    compile("io.springfox:springfox-swagger-ui:2.9.2")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")

    testCompile("com.google.guava:guava:23.0") // ?
    testCompile("com.epages:restdocs-api-spec-mockmvc:0.9.5")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
//        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitJupiterVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter:${junitJupiterVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

val snippetsDir = file("build/generated-snippets")
val apiHost = "localhost"
val apiPort = "8080"
val apiVersion = "0.1.0"
val apiTitle = "Custom API"
val apiDescription = "Sample demonstrating restdocs-api-spec"
val apiFormat = "json"

configure<com.epages.restdocs.apispec.gradle.OpenApiExtension> {
    host = "$apiHost:$apiPort"
    basePath = "/api/v1/"
    title = apiTitle
    description = apiDescription
    version = apiVersion
    format = apiFormat
}

configure<com.epages.restdocs.apispec.gradle.OpenApi3Extension> {
    setServer("http://$apiHost:$apiPort")
    title = apiTitle
    description = apiDescription
    version = apiVersion
    format = apiFormat
    tagDescriptionsPropertiesFile = "src/test/resources/tags.yaml"
}

configure<com.epages.restdocs.apispec.gradle.PostmanExtension> {
    title = apiTitle
    version = apiVersion
    baseUrl = "http://$apiHost:$apiPort"
}
