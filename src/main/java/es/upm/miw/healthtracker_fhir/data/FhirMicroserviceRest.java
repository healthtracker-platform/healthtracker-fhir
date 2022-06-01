package es.upm.miw.healthtracker_fhir.data;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.JsonNode;
import es.upm.miw.healthtracker_fhir.services.exceptions.BadGatewayException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("fhirClient")
public class FhirMicroserviceRest {

    private final String hapiUrl;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public FhirMicroserviceRest(@Value("${miw.hapi.url}") String url,WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
        this.hapiUrl = url;
    }

    public Mono<ClientResponse> postOnFhir(String uri, Resource resource) {
        return  webClientBuilder.build()
                        .post()
                        .uri(this.hapiUrl+ uri)
                        .header("Content-Type","application/fhir+json")
                        .body(Mono.just(FhirContext.forR4().newJsonParser().setPrettyPrint(true).encodeResourceToString(resource)),String.class)
                        .exchange()
                .onErrorResume(exception ->
                        Mono.error(new BadGatewayException("Unexpected error. HAPI FHIR Microservice. " + exception.getMessage())));
    }

    public Mono<ClientResponse> getOnFhir(String uri) {
        return  webClientBuilder.build()
                .get()
                .uri(this.hapiUrl+ uri)
                .header("Accept","application/fhir+xml;q=1.0, application/fhir+json;q=1.0, application/xml+fhir;q=0.9, application/json+fhir;q=0.9")
                .header("Accept-Charset","utf-8")
                .exchange()
                .onErrorResume(exception ->
                        Mono.error(new BadGatewayException("Unexpected error. HAPI FHIR Microservice. " + exception.getMessage())));
    }
}
