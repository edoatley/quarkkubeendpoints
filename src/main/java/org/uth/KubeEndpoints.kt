package org.uth

import io.fabric8.openshift.client.OpenShiftClient
import java.lang.StringBuffer
import io.fabric8.openshift.api.model.Project
import io.fabric8.kubernetes.api.model.Pod
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/endpoints")
class KubeEndpoints {
    @Inject
    lateinit var client: OpenShiftClient

    @POST
    @Path("/secrets")
    @Produces(MediaType.APPLICATION_JSON)
    fun updatePassword(
        @QueryParam("namespace") namespace: String?,
        @QueryParam("secret-name") secretName: String?,
        @QueryParam("secret-key") secretKey: String?,
        @QueryParam("secret-value") secretValue: String?
    ): String? {

        val secret = client.secrets().inNamespace(namespace).withName(secretName)
            .takeIf { s -> s.get().data.containsKey(secretKey) }
            .edit { s -> s.get().data.set(secretKey, secretValue) }
            .
    }

    @GET
    @Path("/pods")
    @Produces(MediaType.TEXT_PLAIN)
    fun envtest(
        @DefaultValue("default") @QueryParam("namespace") namespace: String,
        @DefaultValue("false") @QueryParam("list") listProjects: Boolean
    ): String {
        println(namespace)
        println("Found " + client!!.projects().list().items.size + " projects...")
        val response = StringBuffer()

        // Only render the project list if the parameter indicates to
        if (listProjects) {
            for (project in client!!.projects().list().items) {
                response.append(
                    """
    ${project.metadata.name}
    
    """.trimIndent()
                )
            }
        }
        response.append("\nTargeting $namespace\n")
        for (pod in client!!.pods().inNamespace(namespace).list().items) {
            //response.append( pod.toString() + "\n" );
            //response.append( pod.getMetadata().toString() + "\n" );
            response.append(
                """
    ${pod.metadata.name}, ${pod.metadata.labels}
    
    """.trimIndent()
            )
        }
        return response.toString()
    }
}