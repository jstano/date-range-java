plugins {
  id("java-library")
  id("groovy")
  id("jacoco")
  id("org.sonarqube") version "6.2.0.5505"
  id("maven-publish")
  id("signing")
}

dependencies {
  implementation("commons-cli:commons-cli:1.9.0")
  implementation("commons-io:commons-io:2.19.0")
  implementation("org.apache.commons:commons-collections4:4.5.0")
  implementation("org.apache.commons:commons-lang3:3.17.0")
  implementation("org.apache.groovy:groovy-all:4.0.27")
  implementation("org.jooq:joor-java-8:0.9.15")
  implementation("org.objenesis:objenesis:3.4")
  implementation("org.slf4j:slf4j-api:2.0.17")

  testImplementation("net.bytebuddy:byte-buddy:1.17.6")
  testImplementation("org.junit.jupiter:junit-jupiter:5.13.2")
  testImplementation("org.junit.platform:junit-platform-launcher:1.13.2")
  testImplementation("org.mockito:mockito-junit-jupiter:5.18.0")
  testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")

  testImplementation(files("src/test/resources/test-jar1.jar"))
  testImplementation(files("src/test/resources/test-jar2.jar"))
  testImplementation(files("src/test/resources/test-jarjar.jar"))
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
        name.set("java-utils")
        description.set("A set of utility classes for Java applications")
        url.set("https://github.com/jstano/java-utils")
        licenses {
          license {
            name.set("MIT License")
            url.set("https://opensource.org/license/mit")
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
          connection.set("scm:git:https://github.com/jstano/java-utils.git")
          developerConnection.set("scm:git:ssh://git@github.com:jstano/java-utils.git")
          url.set("https://github.com/jstano/java-utils")
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
//  val signingKeyFile = findProperty("signing.keyFile")?.let { file(it.toString()) }
//  useInMemoryPgpKeys(
//    findProperty("signing.keyId") as String?,
//    signingKeyFile?.readText(),
//    findProperty("signing.password") as String?
//
//  )
  sign(publishing.publications["mavenJava"])
}

sonar {
  val sonarHost = "http://localhost:9000"
  val sonarToken = "sqa_010b94573806de8eaf377006538b63f2b1ebba40"

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
