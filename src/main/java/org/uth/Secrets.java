package org.uth;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.openshift.client.OpenShiftClient;
import org.uth.bean.SecretUpdateBean;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/secrets")
public class Secrets {
  @Inject OpenShiftClient client;

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Secret updatePassword(@BeanParam SecretUpdateBean secretUpdateBean) {
    Secret existingSecret = client.secrets().inNamespace(namespace).withName(secretName).get();

    if (existingSecret.getData().containsKey(secretKey)) {
      existingSecret.getData().put(secretKey, secretValue);
    }

    return existingSecret;
  }
}
