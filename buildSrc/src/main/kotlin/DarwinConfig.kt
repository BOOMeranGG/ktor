/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
import org.gradle.api.*
import org.gradle.kotlin.dsl.*

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
 */

fun Project.configureDarwin() {
    extra.set("hasNative", true)
}
