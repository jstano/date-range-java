plugins {
  id("java-library")
  id("groovy")
  id("jacoco")
  id("org.sonarqube") version "6.3.1.5724"
  id("maven-publish")
  id("signing")
}

dependencies {
  testImplementation("net.bytebuddy:byte-buddy:1.17.7")
  testImplementation("org.apache.groovy:groovy-all:4.0.28")
  testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
  testImplementation("org.junit.platform:junit-platform-launcher:1.13.4")
  testImplementation("org.mockito:mockito-junit-jupiter:5.19.0")
  testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")
}

java {
  withJavadocJar()
  withSourcesJar()
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      pom {
        name.set("date-range")
        description.set("A set of classes that implement ranges for dates, times, and date/times")
        url.set("https://github.com/jstano/date-range-java")
        licenses {
          license {
            name.set("APACHE LICENSE, VERSION 2.0")
            url.set("https://www.apache.org/licenses/LICENSE-2.0")
          }
        }
        developers {
          developer {
            id.set("jstano")
            name.set("Jeff Stano")
            email.set("jeff@stano.com")
          }
        }
        scm {
          connection.set("scm:git:https://github.com/jstano/date-range-java.git")
          developerConnection.set("scm:git:ssh://git@github.com:jstano/date-range-java.git")
          url.set("https://github.com/jstano/date-range-java")
        }
      }
    }
  }
  repositories {
    maven {
      url = uri(layout.buildDirectory.dir("staging-deploy").get().toString())
    }
  }
}

signing {
  sign(publishing.publications["mavenJava"])
}

sonar {
  val extraProperties = extensions.extraProperties.properties
  val sonarHost = extraProperties["com.stano.sonar.host"].toString()
  val sonarToken = extraProperties["com.stano.sonar.token"].toString()

  properties {
    property("sonar.host.url", sonarHost)
    property("sonar.token", sonarToken)
    property("sonar.projectName", "date-range")
    property("sonar.projectKey", "${project.group}:date-range")
    property("sonar.projectVersion", project.version)
  }
}

tasks.register<Zip>("zipStagingDeploy") {
  archiveFileName.set("staging-deploy.zip")
  destinationDirectory.set(layout.buildDirectory.dir("tmp"))
  from("build/staging-deploy") {
    include("**/*")
  }
}

configurations {
  all {
    exclude(group = "commons-logging", module = "commons-logging")
  }
}

tasks.withType<JavaCompile>().configureEach {
  options.compilerArgs = compilerOptions()
  sourceCompatibility = "21"
  targetCompatibility = "21"
}
tasks.withType<GroovyCompile>().configureEach {
  options.compilerArgs = compilerOptions()
  sourceCompatibility = "21"
  targetCompatibility = "21"
  groovyOptions.setParameters(true)
}

tasks.withType<Jar> {
  exclude("**/.gitkeep")
}
tasks.withType<Javadoc>().configureEach {
  (options as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
}
tasks.withType<Test>().configureEach {
  useJUnitPlatform()
  jvmArgs("--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED", "--add-opens", "java.base/java.lang=ALL-UNNAMED")
  finalizedBy("jacocoTestReport")
}
tasks.withType<JacocoReport>().configureEach {
  reports {
    html.required.set(true)
    xml.required.set(true)
  }
}

fun compilerOptions(): List<String> = listOf("-Xlint:none", "-Xdoclint:none", "-nowarn", "-parameters")
