plugins {
    id("org.springframework.boot") version "2.1.9.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    java
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
//sourceCompatibility = "10"

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

/*tasks.jar {
    manifest {
        attributes(Pair("Main-Class", "ru.exrates.ExRatesApplication"))
       // attributes "Main-Class": "ru.exrates.ExRatesApplication"
    }
    from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }
}*/
