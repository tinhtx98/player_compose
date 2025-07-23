pluginManagement {
    repositories {
        google() {
            content {
                includeGroupByRegex("androidx\\..*")
                includeGroupByRegex("com\\.android(\\..*|)")
                includeGroupByRegex("com\\.google\\.android\\..*")
                includeGroupByRegex("com\\.google\\.firebase(\\..*|)")
                includeGroupByRegex("com\\.google\\.gms(\\..*|)")
                includeGroupByRegex("com\\.google\\.mlkit")
                includeGroupByRegex("com\\.google\\.oboe")
                includeGroupByRegex("com\\.google\\.prefab")
                includeGroupByRegex("com\\.google\\.testing\\.platform")
            }
            mavenContent {
                releasesOnly()
            }
        }

        // fetch dagger plugin only
        mavenCentral() {
            content {
                includeGroup("com.google.dagger")
                includeGroup("com.google.dagger.hilt.android")
            }
            mavenContent {
                releasesOnly()
            }
        }

        maven {
            url = uri("https://jitpack.io")
        }

        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // fetch libraries from google maven (https://maven.google.com)
        google() {
            content {
                includeGroupByRegex("androidx\\..*")
                includeGroupByRegex("com\\.android(\\..*|)")
                includeGroupByRegex("com\\.google\\.android\\..*")
                includeGroupByRegex("com\\.google\\.firebase(\\..*|)")
                includeGroupByRegex("com\\.google\\.gms(\\..*|)")
                includeGroupByRegex("com\\.google\\.mlkit")
                includeGroupByRegex("com\\.google\\.oboe")
                includeGroupByRegex("com\\.google\\.prefab")
                includeGroupByRegex("com\\.google\\.testing\\.platform")
            }
            mavenContent {
                releasesOnly()
            }
        }

        maven {
            url = uri("https://jitpack.io")
        }

        // fetch libraries from maven central
        mavenCentral() {
            mavenContent {
                releasesOnly()
            }
        }
    }
}

rootProject.name = "TinhTX Player"
include(":app")
include(":core")
include(":domain")
include(":data")
include(":presentation")
include(":media")
include(":feature")
