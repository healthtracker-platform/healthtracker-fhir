package es.upm.miw.betca_tpv_core.infrastructure.rest_client;

import es.upm.miw.betca_tpv_core.domain.exceptions.BadGatewayException;
import es.upm.miw.betca_tpv_core.domain.exceptions.ForbiddenException;
import es.upm.miw.betca_tpv_core.domain.model.SlackPublication;
import es.upm.miw.betca_tpv_core.domain.rest.SlackMicroservice;
import es.upm.miw.betca_tpv_core.domain.services.utils.SlackMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service("slackClient")
public class SlackMicroserviceRest implements SlackMicroservice {

    private final String slackUri;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public SlackMicroserviceRest(@Value("${miw.tpv.slack.url}") String slackUri, WebClient.Builder webClientBuilder) {
        this.slackUri = slackUri;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Void> publish(SlackPublication slackPublication) {
        if (this.slackUri.equals("SLACK_URI-not-found")) {
            return Mono.error(new ForbiddenException(
                    "Unauthorized Slack Message - Configure environment variable ${SLACK_URI}"));
        }
        String message = SlackMessageBuilder.generateMessage(slackPublication);
        return this.postOnSlack(message)
                .flatMap(response -> {
                    if (HttpStatus.UNAUTHORIZED.equals(response.statusCode())) {
                        return Mono.error(new ForbiddenException("Unauthorized Slack Message"));
                    } else if (response.statusCode().isError()) {
                        return Mono.error(new BadGatewayException("Unexpected error: Slack Microservice. - "
                                            + response.statusCode()));
                    } else {
                        return Mono.empty();
                    }
                });
    }

    private Mono<ClientResponse> postOnSlack(String message) {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication().getCredentials())
                .flatMap(token -> webClientBuilder.build()
                        .mutate().defaultHeader("Authorization", "Bearer " + token).build()
                        .post()
                        .uri(slackUri)
                        .body(Mono.just(message), String.class)
                        .exchange())
                .onErrorResume(exception ->
                        Mono.error(new BadGatewayException("Unexpected error. Slack Microservice. " + exception.getMessage())));
    }
}
