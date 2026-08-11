import com.stano.gradle.mavencentralpublish.MavenCentralPublishExtension

plugins {
  id("com.stano.base")
  id("com.stano.java-library")
  id("com.stano.maven-central-publish")
  id("com.stano.sonar")
}

dependencies {
  testImplementation("net.bytebuddy:byte-buddy:1.18.10")
  testImplementation("org.junit.jupiter:junit-jupiter:6.1.0")
  testImplementation("org.junit.platform:junit-platform-launcher:6.1.0")
  testImplementation("org.mockito:mockito-junit-jupiter:5.23.0")
}

extensions.configure<MavenCentralPublishExtension> {
  componentName = "java"
  pomName = "date-range"
  pomDescription = "A set of classes that implement ranges for dates, times, and date/times"
  pomUrl = "https://github.com/jstano/date-range-java"
  licenseName = "APACHE LICENSE, VERSION 2.0"
  licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0"
  developerId = "jstano"
  developerName = "Jeff Stano"
  developerEmail = "jeff@stano.com"
  scmConnection = "scm:git:https://github.com/jstano/date-range-java.git"
  scmDeveloperConnection = "scm:git:ssh://git@github.com:jstano/date-range-java.git"
  scmUrl = "https://github.com/jstano/date-range-java"
}

configurations {
  all {
    exclude(group = "commons-logging", module = "commons-logging")
  }
}

tasks.withType<Test>().configureEach {
  jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED")
}

tasks.register("formatCheck") {
  dependsOn("spotlessCheck")
  description = "Check code formatting without making changes"
}

tasks.register("formatAll") {
  dependsOn("spotlessApply")
  description = "Apply code formatting to all files"
}

fun compilerOptions(): List<String> = listOf("-Xlint:none", "-Xdoclint:none", "-nowarn", "-parameters")
