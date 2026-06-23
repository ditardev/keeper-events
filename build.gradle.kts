plugins {
	val kotlinVersion = "2.2.21"

	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	kotlin("kapt") version kotlinVersion

	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.micro"
version = "0.0.1"
description = "keeper-micro-events"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("org.springframework.boot:spring-boot-starter-web:3.4.4")

	//Eureka
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:4.2.1")

	//Postgres
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.4")
	implementation("org.postgresql:postgresql:42.7.7")
	runtimeOnly("org.flywaydb:flyway-database-postgresql:11.7.0")

	//UUID
	implementation("com.fasterxml.uuid:java-uuid-generator:5.1.0")

	// Observability & Logging
	implementation("io.micrometer:micrometer-registry-prometheus:1.14.5")
	implementation("com.github.loki4j:loki-logback-appender:1.5.2")

	//Addiction
	implementation("io.micrometer:micrometer-registry-prometheus:1.14.5")
	implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.4")
	implementation("org.springframework.boot:spring-boot-devtools:3.4.4")
	implementation("org.springframework.boot:spring-boot-configuration-processor:3.4.4")
	implementation("joda-time:joda-time:2.14.0")

	//Logging
	implementation("com.github.loki4j:loki-logback-appender:1.5.2")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks {
	jar {
		enabled = false
	}
}


tasks.withType<ProcessResources> {
	inputs.property("version", project.version) // Помогает Gradle кэшировать задачу

	filesMatching("**/application.yml") {
		filter { line ->
			line.replace("\${projectVersion}", project.version.toString())
		}
	}
}