package es.upm.miw.healthtracker_fhir.services;

import es.upm.miw.healthtracker_fhir.TestConfig;
import es.upm.miw.healthtracker_fhir.data.model.SlackPublication;
import es.upm.miw.healthtracker_fhir.data.model.SlackPublicationCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

@TestConfig
class SlackServiceIT {

    @Autowired
    private SlackService slackService;
    @MockBean
    private SlackService slackMicroservice;

    private SlackPublication slackPublication;

    @BeforeEach
    void setUp() {
        this.slackPublication = new SlackPublication("titulo", "autor", "username",
                "email", SlackPublicationCategory.CRITICAL, "message");

        BDDMockito.given(this.slackMicroservice.publish(any(SlackPublication.class)))
                .willAnswer(arguments -> Mono.empty());

    }

    @Test
    void testPublish() {
        StepVerifier
                .create(this.slackService.publish(this.slackPublication))
                .verifyComplete();
    }


}
