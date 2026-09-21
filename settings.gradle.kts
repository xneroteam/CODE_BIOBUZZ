pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BIOBUZZ"
include(":app")

include(":FtcRobotController")
project(":FtcRobotController").projectDir = file("FtcRobotController/FtcRobotController")
include(":TeamCode")
project(":TeamCode").projectDir = file("FtcRobotController/TeamCode")
