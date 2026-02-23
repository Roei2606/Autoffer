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
    plugins {
        id("androidx.navigation.safeargs") version "2.7.7"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "autofferAndroid"
include(":app")
include(":chatSDK")
include(":pricingSDK")
include(":rsocketSDK")
include(":adsSDK")
include(":projectsSDK")
include(":localProjectSDK")
include(":usersSDK")
include(":coremodelsSDK")
