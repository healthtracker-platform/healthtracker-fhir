package es.upm.miw.healthtracker_fhir.data;

import com.fasterxml.jackson.databind.JsonNode;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@Service("azureClient")
public class AzureFhirMicroserviceRest {

    private final String azureUrl;
    private final String azureResource;
    private final String azureClient;
    private final String azureSecret;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public AzureFhirMicroserviceRest(@Value("${miw.azure.url}") String url,
                                     @Value("${miw.azure.resource}") String resource,
                                     @Value("${miw.azure.id}") String clientId,
                                     @Value("${miw.azure.secret}") String clientSecret,
                                     WebClient.Builder webClientBuilder) {
        this.azureUrl = url;
        this.azureResource = resource;
        this.azureClient = clientId;
        this.azureSecret = clientSecret;
        this.webClientBuilder = webClientBuilder;
    }

    public String getToken() {
        try {
            return webClientBuilder.build().post()
                    .uri(this.azureUrl)
                    .header("Content-Type","application/x-www-form-urlencoded")
                    .body(BodyInserters.fromFormData("grant_type","Client_Credentials")
                            .with("client_id",this.azureClient)
                            .with("client_secret",this.azureSecret)
                            .with("resource",this.azureResource))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(token->token.get("access_token").toString().replace('"',' ')).toFuture().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "";
        } catch (ExecutionException e) {
            e.printStackTrace();
            return "";
        }
    }

}