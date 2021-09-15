/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import org.gradle.api.*
import org.gradle.jvm.tasks.*
import org.gradle.kotlin.dsl.*

/*
def jdk = KtorBuildProperties.projectJdk(name)

String lookupJdk(int startingFrom) {
    return (startingFrom..20)
        .collect { it < 10 ? "JDK_1_$it" : "JDK_$it" }
        .collect { System.env[it.toString()] }
        .find { it != null && new File(it).exists() }
}

def jdkHome = lookupJdk(jdk)

kotlin {
    configure(targets) {
        compilations.all { compilation ->
            if (compilation.name == "main") {
                def compileTask = tasks.getByName(compilation.compileKotlinTaskName)
                if (!compileTask.name.endsWith("Jvm")) return

                if (jdkHome != null) {
                    compileTask.kotlinOptions.jdkHome = jdkHome
                }
            }
        }
    }
}

dependencies {
    if (jdk > 7) {
        jvmMainApi(group: 'org.jetbrains.kotlinx', name: 'kotlinx-coroutines-jdk8', version: coroutines_version) {
            exclude(module: 'kotlin-stdlib')
            exclude(module: 'kotlin-stdlib-jvm')
            exclude(module: 'kotlin-stdlib-jdk8')
            exclude(module: 'kotlin-stdlib-jdk7')
        }
    }
}

jvmTest {
    ignoreFailures = true
    maxHeapSize = "2g"
    /*exclude("**/*StressTest*")

}

task stressTest(type: Test) {
    classpath = files { jvmTest.classpath }
    testClassesDirs = files { jvmTest.testClassesDirs }

    ignoreFailures = true
    maxHeapSize = "2g"
    forkEvery = 1
    /*include("**/*StressTest*") 
    systemProperty "enable.stress.tests", "true"
}

configurations {
    testOutput.extendsFrom(testCompile)
    boot
}

jvmJar {
    manifest {
        attributes("Implementation-Title": project.name, "Implementation-Version": configuredVersion)
    }
}
 */

fun Project.configureJvm() {
    val jdk = 8
    val kotlin_version: String by extra
    val slf4j_version: String by extra
    val junit_version: String by extra
    val coroutines_version: String by extra

    kotlin {
        jvm()
        sourceSets.apply {
            val jvmMain by getting {
                dependencies {
                    api("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
                    if (jdk > 6) {
                        api("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version")
                    }
                    if (jdk > 7) {
                        api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
                        api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutines_version")
                    }

                    api("org.slf4j:slf4j-api:$slf4j_version")
                }
            }

            val jvmTest by getting {
                dependencies {
                    api("org.jetbrains.kotlin:kotlin-test")
                    api("org.jetbrains.kotlin:kotlin-test-junit")
                    api("junit:junit:$junit_version")

                    api("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
                    api("junit:junit:$junit_version")

                    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:$coroutines_version")
                }
            }
        }
    }

    tasks.register<Jar>("jarTest") {
        dependsOn(tasks.getByName("jvmTestClasses"))
        classifier = "test"
        from(kotlin.targets.getByName("jvm").compilations.getByName("test").output)
    }
}
