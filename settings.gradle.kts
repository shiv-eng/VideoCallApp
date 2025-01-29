pluginManagement {
    repositories {
        google()
        mavenCentral()
        // Needed for Jitsi artifacts:
        maven { url = uri("https://github.com/jitsi/jitsi-maven-repository/raw/master/releases") }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Jitsi repository again to be safe:
        maven { url = uri("https://github.com/jitsi/jitsi-maven-repository/raw/master/releases") }
    }
}
rootProject.name = "VideoCallingApp"
include(":app")
