@file:Suppress("UNUSED_VARIABLE")

import org.gradle.api.*
import org.gradle.kotlin.dsl.*

/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

fun Project.configureTargets() {
    kotlin {
        jvm()
        js(IR) {
            nodejs()
            browser()
        }

        posixTargets()

        if (IDEA_ACTIVE) {
            createIdeaTarget("posix")
            createIdeaTarget("nix")
            createIdeaTarget("darwin")
            createIdeaTarget("desktop")
        } else {
            sourceSets {
                val posixMain by creating
                val posixTest by creating
                val nixMain by creating
                val nixTest by creating
                val darwinMain by creating
                val darwinTest by creating
                val desktopMain by creating
                val desktopTest by creating
            }
        }

        sourceSets {
            val commonMain by getting
            val commonTest by getting

            val jvmAndNixMain by creating {
                dependsOn(commonMain)
            }

            val jvmAndNixTest by creating

            val jvmMain by getting {
                dependsOn(jvmAndNixMain)
            }

            val jvmTest by getting {
                dependsOn(jvmAndNixTest)
            }

            val posixMain by getting {
                dependsOn(commonMain)
            }

            val posixTest by getting

            val nixMain by getting {
                dependsOn(posixMain)
                dependsOn(jvmAndNixMain)
            }

            val nixTest by getting {
                dependsOn(jvmAndNixTest)
            }

            val darwinMain by getting {
                dependsOn(nixMain)
            }

            val darwinTest by getting

            val desktopMain by getting {
                dependsOn(posixMain)
            }

            val desktopTest by getting

            posixTargets().forEach {
                getByName("${it.name}Main").dependsOn(posixMain)
                getByName("${it.name}Test").dependsOn(posixTest)
            }

            nixTargets().forEach {
                getByName("${it.name}Main").dependsOn(nixMain)
                getByName("${it.name}Test").dependsOn(nixTest)
            }

            darwinTargets().forEach {
                getByName("${it.name}Main").dependsOn(darwinMain)
                getByName("${it.name}Test").dependsOn(darwinTest)
            }

            desktopTargets().forEach {
                getByName("${it.name}Main").dependsOn(desktopMain)
                getByName("${it.name}Test").dependsOn(desktopTest)

                if (!it.name.startsWith(HOST_NAME)) {
                    disableCompilation(it)
                }
            }
        }
    }
}
