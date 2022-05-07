package es.upm.miw.healthtracker_fhir.data;

import com.fasterxml.jackson.databind.JsonNode;
import es.upm.miw.healthtracker_fhir.services.exceptions.BadGatewayException;
import es.upm.miw.healthtracker_fhir.services.exceptions.ForbiddenException;
import es.upm.miw.healthtracker_fhir.data.model.SlackPublication;
import es.upm.miw.healthtracker_fhir.services.utils.SlackMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service("slackClient")
public class SlackMicroserviceRest {


    private final String azureUrl;
    private final String azureResource;
    private final String azureClient;
    private final String azureSecret;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public SlackMicroserviceRest(@Value("${miw.azure.url}") String url,
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


    public Mono<Void> publish(SlackPublication slackPublication) {
        String message = SlackMessageBuilder.generateMessage(slackPublication);
        return this.postOnSlack(message)
                .flatMap(response -> {
                    if (HttpStatus.UNAUTHORIZED.equals(response.statusCode())) {
                        return Mono.error(new ForbiddenException("Unauthorized Slack Message"));
                    } else if (response.statusCode().isError()) {
                        return Mono.error(new BadGatewayException("Unexpected error: Slack Microservice. - "
                                            + response.statusCode()));
                    }
                    else {
                        return Mono.empty();
                    }
                });
    }

    private Mono<ClientResponse> postOnSlack(String message) {

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
                        .get()
                        .uri(this.azureResource+"/Patient")
                        .exchange()
                .onErrorResume(exception ->
                        Mono.error(new BadGatewayException("Unexpected error. Slack Microservice. " + exception.getMessage()))));
    }
}
