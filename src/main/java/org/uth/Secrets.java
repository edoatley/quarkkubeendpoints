package org.uth;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.openshift.client.OpenShiftClient;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/secrets")
public class Secrets {
  @Inject OpenShiftClient client;

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Secret updatePassword(
      @QueryParam("namespace") String namespace,
      @QueryParam("secret-name") String secretName,
      @QueryParam("secret-key") String secretKey,
      @QueryParam("secret-value") String secretValue) {
    Secret existingSecret = client.secrets().inNamespace(namespace).withName(secretName).get();

    if (existingSecret.getData().containsKey(secretKey)) {
      existingSecret.getData().put(secretKey, secretValue);
    }

    return existingSecret;
  }
}
