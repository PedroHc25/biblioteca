plugins {
	java
	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "pe.edu.utp"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
//WEB/API REST//
    implementation("org.springframework.boot:spring-boot-starter-webmvc")

//BD-JPA-HIBERNATE//
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

//BD-MYSQL//
    runtimeOnly("com.mysql:mysql-connector-j")

//SECURITY
implementation("org.springframework.boot:spring-boot-starter-security")

//JWT
implementation("io.jsonwebtoken:jjwt-api:0.12.6")
runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
//PRUEBAS//
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("passed", "failed", "skipped")
    }

    afterSuite(KotlinClosure2<TestDescriptor, TestResult, Unit>({ descriptor, result ->
        if (descriptor.parent == null) {
            println("========================================")
            println("TOTAL DE TESTS: ${result.testCount}")
            println("TESTS FALLIDOS: ${result.failedTestCount}")
            println("TESTS EXITOSOS: ${result.successfulTestCount}")
            println("========================================")
        }
    }))
}