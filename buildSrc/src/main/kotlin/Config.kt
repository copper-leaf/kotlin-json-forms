import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.provideDelegate

object Config {
    val groupId = "io.github.copper-leaf"
    val githubUrl = "https://github.com/copper-leaf/json-forms"
    val license = License.BSD3
    val javaVersion = JavaVersion.VERSION_11.toString()

    object Developer {
        val id = "cjbrooks12"
        val name = "Casey Brooks"
        val email = "cjbrooks12@gmail.com"
    }

    fun projectVersion(project: Project): ProjectVersion {
        val projectVersion: ProjectVersion by project.extra
        return projectVersion
    }

    fun publishConfiguration(project: Project): PublishConfiguration {
        return PublishConfiguration(
            githubUser = project.loadProperty("github_username", "GITHUB_ACTOR"),
            githubToken = project.loadProperty("github_token", "GITHUB_TOKEN"),

            mavenRepositoryBaseUrl = "https://s01.oss.sonatype.org",
            stagingRepositoryIdFile = project.rootProject.buildDir.resolve("export").resolve("stagingRepositoryId"),
            stagingProfileId = project.loadProperty("staging_profile_id"),

            signingKeyId = project.loadProperty("signing_key_id"),
            signingKey = project.loadFileContents("signing_key"),
            signingPassword = project.loadProperty("signing_password"),
            ossrhUsername = project.loadProperty("ossrh_username"),
            ossrhPassword = project.loadProperty("ossrh_password"),

            jetbrainsMarketplacePassphrase = project.loadProperty("jb_passphrase"),
            jetbrainsMarketplacePrivateKey = project.loadFileContents("jb_signing_key"),
            jetbrainsMarketplaceCertificateChain = project.loadFileContents("jb_chain"),
            jetbrainsMarketplaceToken = project.loadProperty("jb_marketplace_token"),
        )
    }

    fun customProperties(project: Project): Map<String, Any> {
        return project
            .properties
            .entries
            .filter { it.key.startsWith("copperleaf") && it.value != null }
            .map {
                val stringValue = it.value!!.toString()
                val booleanValue = when (stringValue) {
                    "true" -> true
                    "false" -> false
                    else -> null
                }

                it.key to (booleanValue ?: stringValue)
            }
            .toMap()
    }
}
