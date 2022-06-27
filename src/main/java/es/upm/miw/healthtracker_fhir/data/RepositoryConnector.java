package es.upm.miw.healthtracker_fhir.data;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;

import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;


@Service("fhirClient")
public class RepositoryConnector {

  private static final Logger logger = LoggerFactory.getLogger(RepositoryConnector.class);

  private String url;
  private  FhirContext ctx;
  private IGenericClient client;


  @Autowired
  public RepositoryConnector(@Value("${miw.healthtracker.repo}") String url, AzureFhirMicroserviceRest azureFhirMicroserviceRest) {
    this.ctx = FhirContext.forR4(); // Expensive
    logger.info("Repository endpoint: {}", url);
    IRestfulClientFactory clientFactory = ctx.getRestfulClientFactory();
    String tok = azureFhirMicroserviceRest.getToken();
    BearerTokenAuthInterceptor authInterceptor = new BearerTokenAuthInterceptor(tok);
    this.client = ctx.newRestfulGenericClient(url);
    this.client.registerInterceptor(authInterceptor);
  }


  /**
   * Generic method for creating a resource.
   * 
   * @param resource resource
   * @return id for new resource
   */
  public String createResource(IBaseResource resource) {
    IIdType id = client.create().resource(resource).execute().getId();
    logger.debug("Resource [{}] created with id [{}]", resource.getClass().toGenericString(), id);
    return id.getIdPart();
  }


  public Bundle search(String query) {
    Bundle bundle = client.search().byUrl(query).returnBundle(Bundle.class).execute();
    logger.debug("Search resource [{}]", query);
    return bundle;
  }

  public <T extends IBaseResource> T readResource(IIdType id, Class<T> type) {
    logger.debug("Reading resource [{}] with id [{}]", type.getClass().toGenericString(),
        id.getIdPart());
    return client.read().resource(type).withId(id).execute();
  }


  public MethodOutcome updateResource(IBaseResource resource) {
    logger.debug("Updating resource [{}] with id [{}]", resource.getClass().toGenericString(),
        resource.getIdElement());
    return client.update().resource(resource).execute();
  }


  public MethodOutcome deleteResource(IBaseResource resource) {
    logger.debug("Deleting resource [{}] with id [{}]", resource.getClass().toGenericString(),
        resource.getIdElement());
    return (MethodOutcome) client.delete().resource(resource).execute();
  }

  public Bundle executeTransactionWithBundle(Bundle bundle) {
    logger.debug("Creating bundle in repository: {}",
        ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
    Bundle resp = client.transaction().withBundle(bundle).execute();
    return resp;
  }


  private String encodeResourceToString(IBaseResource resource) {
    return ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resource);
  }

  public IGenericClient client() {
    return client;
  }

}

