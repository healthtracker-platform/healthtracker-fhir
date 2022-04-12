package es.upm.miw.betca_tpv_core.infrastructure.api.resources;

import es.upm.miw.betca_tpv_core.domain.exceptions.ForbiddenException;
import es.upm.miw.betca_tpv_core.domain.model.SlackPublication;
import es.upm.miw.betca_tpv_core.domain.model.SlackPublicationCategory;
import es.upm.miw.betca_tpv_core.domain.rest.SlackMicroservice;
import es.upm.miw.betca_tpv_core.infrastructure.api.RestClientTestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@RestTestConfig
class SlackResourceIT {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private RestClientTestService restClientTestService;
    @MockBean
    private SlackMicroservice slackMicroservice;

    private SlackPublication slackPublication;

    @BeforeEach
    void setUp() {
        this.slackPublication = new SlackPublication("titulo", "autor", "username",
                "email", SlackPublicationCategory.CRITICAL, "message");
    }

    @Test
    void testPublishOk() {
        BDDMockito.given(this.slackMicroservice.publish(any(SlackPublication.class)))
                .willAnswer(arguments -> Mono.empty());

        this.restClientTestService.loginAdmin(webTestClient)
                .post()
                .uri(SlackResource.SLACK)
                .body(Mono.just(this.slackPublication), SlackPublication.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SlackPublication.class);
    }

    @Test
    void testPublishForbidden() {
        BDDMockito.given(this.slackMicroservice.publish(any(SlackPublication.class)))
                .willAnswer(arguments -> Mono.error(new ForbiddenException("Unauthorized Slack Message")));

        this.restClientTestService.loginAdmin(webTestClient)
                .post()
                .uri(SlackResource.SLACK)
                .body(Mono.just(this.slackPublication), SlackPublication.class)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void testPublishUnauthorized() {
        this.webTestClient
                .post()
                .uri(SlackResource.SLACK)
                .body(Mono.just(this.slackPublication), SlackPublication.class)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
