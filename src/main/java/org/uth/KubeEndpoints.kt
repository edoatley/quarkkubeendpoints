package org.uth;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.api.model.Project;
import io.fabric8.openshift.client.OpenShiftClient;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/endpoints")
public class KubeEndpoints
{
  public KubeEndpoints() {}

//  @Inject
//  KubernetesClient client;

  @Inject
  OpenShiftClient client;

  @GET
  @Path("/pods")
  @Produces(MediaType.TEXT_PLAIN)
  public String envtest(@DefaultValue("default") @QueryParam("namespace") String namespace, @DefaultValue("false") @QueryParam("list") boolean listProjects )
  {
    System.out.println( namespace );
    System.out.println( "Found " + client.projects().list().getItems().size() + " projects...");

    StringBuffer response = new StringBuffer();

    // Only render the project list if the parameter indicates to
    if( listProjects )
    {
      for (Project project : client.projects().list().getItems())
      {
        response.append(project.getMetadata().getName() + "\n");
      }
    }

    response.append( "\nTargeting " + namespace + "\n");

    for( Pod pod : client.pods().inNamespace(namespace).list().getItems())
    {
      //response.append( pod.toString() + "\n" );
      //response.append( pod.getMetadata().toString() + "\n" );
      response.append( pod.getMetadata().getName() + ", " + pod.getMetadata().getLabels() + "\n" );
    }

    return response.toString();
  }
}
