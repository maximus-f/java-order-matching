plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // Mockito for mocking
    testImplementation("org.mockito:mockito-core:4.0.0")
    // Add Mockito-inline to mock final classes or static methods
    testImplementation("org.mockito:mockito-inline:4.0.0")
    // Java-WebSocket
    implementation("org.java-websocket:Java-WebSocket:1.5.7")
    // JSON reader jackson lib
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    implementation("ch.qos.logback:logback-classic:1.2.11")


}

tasks.test {
    useJUnitPlatform()
}