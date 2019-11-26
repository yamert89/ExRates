
plugins {
    id("org.springframework.boot") version "2.1.9.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("com.github.onslip.gradle-one-jar") version "1.0.5"
    java
    //kotlin("jvm") version "1.2.31"
    //`build-scan`
}

/*buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    publishAlways()
}*/

group = "ru.exrates"
version = "0.0.1-SNAPSHOT"

java{
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

val developmentOnly = configurations.create("developmentOnly")
configurations {
    developmentOnly
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies (){

    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("mysql:mysql-connector-java")
    runtimeOnly("org.postgresql:postgresql:42.2.8")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testCompile("org.mockito:mockito-core:2.1.0")
    testRuntimeOnly("org.postgresql:postgresql:42.2.8")

}

springBoot{
    
}

tasks{

    /*jar{
        manifest{
            attributes["Main-Class"] = "ru.exrates.ExRatesApplication"
        }
        from(configurations.compileClasspath.get().map{if (it.isDirectory) it else zipTree(it)})
        //from(configurations.compileClasspath.get())
        *//*into("lib"){
            from(configurations.compileClasspath.get())
        }*//*
        archiveFileName.set("my.jar")
        enabled = true
    }*/

    bootJar{
        //mainClassName = "ru.exrates.ExratesApplication"
        archiveFileName.set("demo.jar")
        launchScript()
    }




}




