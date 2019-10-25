import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val epagesRestDocsVer = "0.9.5"
    val springBootVersion = "2.1.9.RELEASE"

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

val snippetsDir = file("build/generated-snippets")

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
    val junitJupiterVersion = "5.4.0"

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    compile("io.springfox:springfox-swagger2:2.9.2")
    compile("io.springfox:springfox-swagger-ui:2.9.2")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")

    testCompile("com.google.guava:guava:23.0") // ?
    testCompile("com.epages:restdocs-api-spec-mockmvc:0.9.5")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
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

// todo config of the plugin
//tasks {
//    getByName<Open>("openapi").host = "localhost:8080"
//        basePath = "/api"
//        title = "My API"
//        description = "An ecommerce sample demonstrating restdocs-api-spec"
//        version = "0.1.0"
//        format = "json"
//    }
//
//    getByName("openapi3") {
//        server = "https://localhost:8080"
//        title = "My API"
//        description = "An ecommerce sample demonstrating restdocs-api-spec"
//        version = "0.1.0"
//        format = "json"
//        tagDescriptionsPropertiesFile = "src/test/resources/tags.yaml"
//    }
//
//    getByName("postman") {
//        title = "My API"
//        version = "0.1.0"
//        baseUrl = "https://localhost:8080"
//    }
//}
