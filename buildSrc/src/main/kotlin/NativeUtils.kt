/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*

fun KotlinMultiplatformExtension.posixTargets(): Set<KotlinNativeTarget> =
    nixTargets() + mingwX64()

fun KotlinMultiplatformExtension.nixTargets(): Set<KotlinNativeTarget> =
    darwinTargets() + linuxX64()

fun KotlinMultiplatformExtension.darwinTargets(): Set<KotlinNativeTarget> = setOf(
    iosX64(),
    iosArm64(),
    iosArm32(),

    watchosX86(),
    watchosX64(),
    watchosArm32(),
    watchosArm64(),

    tvosX64(),
    tvosArm64(),

    macosX64()
)

fun KotlinMultiplatformExtension.desktopTargets(): Set<KotlinNativeTarget> = setOf(
    macosX64(),
    linuxX64(),
    mingwX64()
)
