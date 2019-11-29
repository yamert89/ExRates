import org.hidetake.groovy.ssh.core.Remote

plugins {
    id("org.springframework.boot") version "2.1.9.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("com.github.onslip.gradle-one-jar") version "1.0.5"
    id("org.hidetake.ssh") version "2.10.1"
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
    sourceCompatibility = JavaVersion.VERSION_1_10
    targetCompatibility = JavaVersion.VERSION_1_10
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

remotes{

    val rem = this.create("rem")
    rem.host = "enchat.ru"
    rem.user = "root"
    rem.identity = File("${projectDir}/myKey")
    this.add(rem)

}

tasks{

    bootJar{
        archiveFileName.set("demo.jar")
        launchScript()
    }

    bootJar.get().dependsOn.add("classes")
}

/*val deploy = task("deploy"){
    doLast{
        val cl: groovy.lang.Closure<Remote> by extra
        cl.run { print("ad") }
        val session = org.hidetake.groovy.ssh.session.Session<Remote>(remotes.getByName("rem"), cl)
        ssh.run{
            session.closure
            session.setProperty("put", "")
           *//* val session = org.hidetake.groovy.ssh.session.Session<Remote>(remotes.getByName("rem")){
                *//**//*put from: 'example.war', into: '/webapps'
                execute 'sudo service tomcat restart'*//**//*
            }*//*
        }
       

        
    }
}*/









