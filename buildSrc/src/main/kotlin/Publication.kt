/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import org.gradle.api.*
import org.gradle.kotlin.dsl.*


/*
def pomConfig = {
    licenses {
        license {
            name "The Apache Software License, Version 2.0"
            url "https://www.apache.org/licenses/LICENSE-2.0.txt"
            distribution "repo"
        }
    }
    developers {
        developer {
            id "JetBrains"
            name "JetBrains Team"
            organization "JetBrains"
            organizationUrl "https://www.jetbrains.com"
        }
    }

    scm {
        url "https://github.com/ktorio/ktor.git"
    }
}

project.ext.configureMavenCentralMetadata = { pom ->
    def root = asNode()
    root.appendNode('name', project.name)
    root.appendNode('description', 'Ktor is a framework for quickly creating web applications in Kotlin with minimal effort.')
    root.appendNode('url', 'https://github.com/ktorio/ktor')
    root.children().last() + pomConfig
}

apply plugin: 'maven-publish'

apply from: project.rootProject.file('gradle/pom.gradle')

task emptyJar(type: Jar) {
    archiveAppendix.set("empty")
}

def selectArtifactId(project, type, defaultName) {
    def name = project.name
    def isCommon = project.ext.has("commonStructure")
    def commonIsRoot = project.ext.has("commonStructure") && project.ext.commonStructure
    def hasNative = project.ext.has("hasNative") && project.ext.hasNative

    switch (type) {
        case 'metadata':
            if (!isCommon) return "$name-$type"
            return "$name-metadata"
            break
        case 'kotlinMultiplatform':
            if (!hasNative) return "$name-kotlinMultiplatform"
            return name
            break
        case 'jvm':
            return commonIsRoot ? "$name-jvm" : "$name"
            break
        default:
            return isCommon || hasNative ? defaultName : "$name"
            break
    }
}

def isAvailableForPublication(publication) {
    def name = publication.name
    if (name in ['maven']) return true

    def result = false
    result = result | (name in ['jvm', 'androidRelease', 'androidDebug', 'js', 'jsLegacy', 'metadata', 'kotlinMultiplatform'])
    result = result | (project.ext.isLinuxHost && name == 'linuxX64')
    result = result | (project.ext.isWinHost && name == 'mingwX64')
    result = result | (project.ext.isMacosHost && name in ['iosX64', 'iosArm32', 'iosArm64', 'macosX64', 'watchosArm32', 'watchosArm64', 'watchosX86', 'watchosX64', 'tvosArm64', 'tvosX64'])

    return result
}

tasks.withType(AbstractPublishToMaven).all {
    onlyIf { isAvailableForPublication(publication) }
}

def publishingUser = System.getenv('PUBLISHING_USER')
def publishingPassword = System.getenv('PUBLISHING_PASSWORD')
def publishingUrl = System.getenv("PUBLISHING_URL")

publishing {
    repositories {
        maven {
            if (publishLocal) {
                url globalM2
            } else {
                url = publishingUrl
                credentials {
                    username = publishingUser
                    password = publishingPassword
                }
            }
        }
        maven { name = "testLocal"; url = "$rootProject.buildDir/m2" }
    }
    publications.all {
        pom.withXml(configureMavenCentralMetadata)

        def type = it.name
        def id = selectArtifactId(project, type, it.artifactId)
        it.artifactId = id

        if (name == "kotlinMultiplatform") {
            it.artifact(emptyJar) { classifier 'javadoc' }
            it.artifact(emptyJar) { classifier 'kdoc' }
        }
    }

    if (rootProject.ext.nonDefaultProjectStructure.contains(project.name)) return

    kotlin.targets.all { target ->
        def publication = publishing.publications.findByName(target.name)

        if (publication != null) {
            if (target.platformType.name == 'jvm') {
                publication.artifact(emptyJar) {
                    classifier 'javadoc'
                }
            } else {
                publication.artifact(emptyJar) {
                    classifier 'javadoc'
                }
                publication.artifact(emptyJar) {
                    classifier 'kdoc'
                }
            }

            if (target.platformType.name == 'native') {
                publication.artifact emptyJar
            }
        }
    }
}

publish.dependsOn publishToMavenLocal


def signingKey = System.getenv("SIGN_KEY_ID")
def signingKeyPassphrase = System.getenv("SIGN_KEY_PASSPHRASE")

if (signingKey != null && signingKey != "") {
    project.ext["signing.gnupg.keyName"] = signingKey
    project.ext["signing.gnupg.passphrase"] = signingKeyPassphrase

    apply plugin: 'signing'

    signing {
        useGpgCmd()
        sign publishing.publications
    }
}
 */

fun Project.configurePublication() {
    apply(plugin = "maven-publish")
}
