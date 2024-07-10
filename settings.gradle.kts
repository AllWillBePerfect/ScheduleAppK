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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ScheduleAppK"
include(":app")
include(":core:network")
include(":core:database")
include(":features:enter")
include(":core:values")
include(":core:views")
include(":rxtest")
include(":core:data")
include(":core:sharpref")
include(":core:models")
include(":core:domain")
include(":features:schedule")
include(":core:utils")
include(":features:settings")
include(":features:clear")
