/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import org.gradle.api.*
import org.gradle.kotlin.dsl.*

fun Project.configureCommon() {
    val coroutines_version: String by extra
    val kotlin_version: String by extra

    kotlin.sourceSets.apply {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlin:kotlin-stdlib-common")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
            }
        }
        val commonTest by getting {
            dependencies {
                api("org.jetbrains.kotlin:kotlin-test-common:$kotlin_version")
                api("org.jetbrains.kotlin:kotlin-test-annotations-common:$kotlin_version")
            }
        }
    }

    extra.set("commonStructure", true)
}
