
plugins {
    id("org.springframework.boot") version "2.1.9.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("com.github.onslip.gradle-one-jar") version "1.0.5"
    java
    kotlin("jvm") version "1.2.31"
    `build-scan`
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    publishAlways()
}

group = "ru.exrates"
version = "0.0.1-SNAPSHOT"

java{
    sourceCompatibility = JavaVersion.VERSION_1_10
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

tasks{

    /*val myOneJar by creating(com.github.rholder.gradle.task.OneJar::class){
        mainClass = "ru.exrates.ExRatesApplication"
        //archiveFileName.set("My2.jar")
        destinationDirectory.set(File("J:/outJar/"))
        dependsOn.add(named("jar"))
    }*/

    val myJar by creating(Jar::class) {
        archiveFileName.set("My.jar")
        destinationDirectory.set(File("${buildDir}/outJar/"))
        from(configurations.compileClasspath.get())
        from(compileJava.get())
        //from(configurations.compileClasspath.get().map{if (it.isDirectory) it else zipTree(it)})
        //with(tasks.jar as CopySpec)
        manifest{
            attributes["Main-Class"] = "ru.exrates.ExRatesApplication"
        }
        dependsOn.add(named("jar"))

    }
}



/*tasks{
    register("myJar", Jar::class){
        archiveBaseName
    }

}*/


/*tasks.jar{
    manifest {
        attributes(Pair("Main-Class", "ru.exrates.ExRatesApplication"))
    }
    archiveName = "hello.jar"
    destinationDir = file("${buildDir}/jars")
    
}*/

/*tasks.jar {
    manifest {
        attributes(Pair("Main-Class", "ru.exrates.ExRatesApplication"))
       // attributes "Main-Class": "ru.exrates.ExRatesApplication"
    }
    from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }
}*/
