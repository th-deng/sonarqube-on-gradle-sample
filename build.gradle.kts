// Kotlin DSL sample

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("org.sonarqube") version "2.8"
    kotlin("jvm") version "1.3.61"
    kotlin("plugin.spring") version "1.3.61"

    idea
    java
    jacoco
}

group = "com.woowahan.thdeng.test"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    extensions.configure(JacocoTaskExtension::class) {
        destinationFile = file("$buildDir/jacoco/jacoco.exec")
    }

    finalizedBy("jacocoTestReport")
}

jacoco {
    // JaCoCo 버전
    toolVersion = "0.8.5"

//  테스트결과 리포트를 저장할 경로 변경할 수 있습니다.
//  default는 "${project.reporting.baseDir}/jacoco"
//  reportsDir = file("$buildDir/customJacocoReportDir")
}

tasks.jacocoTestReport {
    reports {
        // 원하는 리포트를 켜고 끌 수 있습니다. - SonarQube에서 xml 파일을 읽어가니 enable 해줍니다.
        html.isEnabled = true
        xml.isEnabled = true
        csv.isEnabled = false

//      각 리포트 타입 마다 리포트 저장 경로를 설정할 수 있습니다. - xml 파일은 SonarQube에서 읽어감
        html.destination = file("$buildDir/reports/jacoco/test/html")
        xml.destination = file("$buildDir/reports/jacoco/test/jacocoTestReport.xml")
    }

    finalizedBy("jacocoTestCoverageVerification")
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            // 'element'가 없으면 프로젝트의 전체 파일을 합친 값을 기준으로 합니다.
            limit {
                // 'counter'를 지정하지 않으면 default는 'INSTRUCTION'
                // 'value'를 지정하지 않으면 default는 'COVEREDRATIO'
                minimum = "0.30".toBigDecimal()
            }
        }

        rule {
            // 룰을 간단히 켜고 끌 수 있습니다.
            enabled = true

            // 룰을 체크할 단위는 클래스 단위
            element = "CLASS"

            // 브랜치 커버리지를 최소한 90% 만족시켜야 합니다.
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.10".toBigDecimal()
            }

            // 라인 커버리지를 최소한 80% 만족시켜야 합니다.
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.20".toBigDecimal()
            }

            // 빈 줄을 제외한 코드의 라인수를 최대 200라인으로 제한합니다.
            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "200".toBigDecimal()
//              maximum = "8".toBigDecimal()
            }

            // 커버리지 체크를 제외할 클래스들 - (클래스명으로 지정)
            excludes = listOf(
//                    "*.test.*",
//                    "*.Kotlin*"
//                    "*.Q*"
            )
        }
    }
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "Runs the unit tests with coverage"

    dependsOn(":test",
            ":jacocoTestReport",
            ":jacocoTestCoverageVerification")

    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}

sonarqube {
    properties {
        property("sonar.host.url", project.findProperty("sonar.host.url") ?: "http://127.0.0.1:9000")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.sources", "src/main")
        property("sonar.tests", "src/test")
//      여러개인 경우 콤마로 구분해서 지정할 수 있습니다.
//      property("sonar.sources", "src/main/kotlin,src/main/java")
//      property("sonar.sources", "src/test/kotlin,src/test/java")

//      sonar.exclusions 로 제외할 파일들을 설정합니다. - (파일명으로 지정)
//      property("sonar.exclusions", "**/Q*.java,**/*Generated.java")

        property("sonar.junit.reportPaths", "$buildDir/test-results/test")
        property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/test/jacocoTestReport.xml")

//      sonar.jacoco.reportPaths 는 deprecated 되었고, sonar.coverage.jacoco.xmlReportPaths 사용합니다.
//      property("sonar.jacoco.reportPaths", "$buildDir/jacoco/jacoco.exec")
    }
}
