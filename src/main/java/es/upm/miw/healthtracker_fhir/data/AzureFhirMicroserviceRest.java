package es.upm.miw.healthtracker_fhir.data;

import com.fasterxml.jackson.databind.JsonNode;
import es.upm.miw.healthtracker_fhir.services.exceptions.BadGatewayException;
import es.upm.miw.healthtracker_fhir.services.exceptions.ForbiddenException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service("azureFhirClient")
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

    public Mono<ClientResponse> postOnAzureFhir(String uri, Resource resource) {
        return webClientBuilder.build().post()
                .uri(this.azureUrl)
                .header("Content-Type","application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("grant_type","Client_Credentials")
                        .with("client_id",this.azureClient)
                        .with("client_secret",this.azureSecret)
                        .with("resource",this.azureResource))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(token -> webClientBuilder.build()
                        .mutate().defaultHeader("Authorization", "Bearer" + token.get("access_token").toString().replace('"',' ')).build()
                        .post()
                        .uri(this.azureResource+ uri)
                        .body(Mono.just(resource), Patient.class)
                        .exchange()
               );
    }

}
