/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import org.gradle.api.*
import org.gradle.api.publish.maven.tasks.*
import org.gradle.api.publish.tasks.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*

/*
assemble.doLast {
    copy {
        if (project.ext.isLinuxHost) {
            from "build/classes/kotlin/linuxX64"
        }
        if (project.ext.isMacosHost) {
            from "build/classes/kotlin/macosX64"
        }
        if (project.ext.isWinHost) {
            from "build/classes/kotlin/mingwX64"
        }

        into "build/classes/kotlin/posix"
    }
}

/**
 * Disable cross compilation for missing cinterop.
 * @param targets
 */
def disableCompilation(targets) {
    configure(targets) {
        compilations.all {
            cinterops.all { project.tasks[interopProcessingTaskName].enabled = false }
            compileKotlinTask.enabled = false
        }
        binaries.all { linkTask.enabled = false }

        mavenPublication { publicationToDisable ->
            tasks.withType(AbstractPublishToMaven).all {
                onlyIf { publication != publicationToDisable }
            }
            tasks.withType(GenerateModuleMetadata).all {
                onlyIf { publication.get() != publicationToDisable }
            }
        }
    }
}

def skipMingwProjects = [
    "ktor-network",
    "ktor-network-tls",
    "ktor-client-cio"
]

def skipMingw = skipMingwProjects.contains(project.name)

if (host == 'windows' && skipMingw) return

kotlin {
    targets {
            def crossCompileTargets = [linuxX64, macosX64]

            if (!skipMingw) {
                crossCompileTargets += [mingwX64]
            }

            switch (host) {
                case 'linux':
                    crossCompileTargets.remove(linuxX64)
                    break
                case 'windows':
                    if (!skipMingw) crossCompileTargets.remove(mingwX64)
                    break
                case 'macos':
                    crossCompileTargets.remove(macosX64)
                    break
            }

            disableCompilation(crossCompileTargets)

            if (!skipMingw) {
                configure([mingwX64Test]) {
                    environment "PATH": "C:\\msys64\\mingw64\\bin;C:\\Tools\\msys64\\mingw64\\bin;C:\\Tools\\msys2\\mingw64\\bin"
                }
            }
        }
    }
}
 */

fun Project.disableCompilation(target: KotlinNativeTarget) {
    target.apply {

        compilations.forEach {
            it.cinterops.forEach { cInterop ->
                tasks.getByName(cInterop.interopProcessingTaskName).enabled = false
            }
        }

        binaries.forEach {
            it.linkTask.enabled = false
        }

        mavenPublication {
            tasks.withType<AbstractPublishToMaven>().all {
                onlyIf { publication != this@mavenPublication }
            }
            tasks.withType<GenerateModuleMetadata>().all {
                onlyIf { publication.get() != this@mavenPublication }
            }
        }
    }
}

fun Project.configurePosix() {
    extra.set("hasNative", true)
}
