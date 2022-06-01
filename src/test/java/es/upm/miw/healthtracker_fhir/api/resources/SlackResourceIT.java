package es.upm.miw.healthtracker_fhir.api.resources;

import es.upm.miw.healthtracker_fhir.services.PatientService;
import es.upm.miw.healthtracker_fhir.api.dtos.Patient;
import es.upm.miw.healthtracker_fhir.api.RestClientTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;

@RestTestConfig
class SlackResourceIT {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private RestClientTestService restClientTestService;
    @MockBean
    private PatientService slackMicroservice;

    private Patient slackPublication;

//    @BeforeEach
//    void setUp() {
//        this.slackPublication = new SlackPublication("titulo", "autor", "username",
//                "email", SlackPublicationCategory.CRITICAL, "message");
//    }
//
//    @Test
//    void testPublishOk() {
//        BDDMockito.given(this.slackMicroservice.publish(any(SlackPublication.class)))
//                .willAnswer(arguments -> Mono.empty());
//
//        this.restClientTestService.loginAdmin(webTestClient)
//                .post()
//                .uri(PatientResource.SLACK)
//                .body(Mono.just(this.slackPublication), SlackPublication.class)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(SlackPublication.class);
//    }
//
//    @Test
//    void testPublishForbidden() {
//        BDDMockito.given(this.slackMicroservice.publish(any(SlackPublication.class)))
//                .willAnswer(arguments -> Mono.error(new ForbiddenException("Unauthorized Slack Message")));
//
//        this.restClientTestService.loginAdmin(webTestClient)
//                .post()
//                .uri(PatientResource.SLACK)
//                .body(Mono.just(this.slackPublication), SlackPublication.class)
//                .exchange()
//                .expectStatus().isForbidden();
//    }
//
//    @Test
//    void testPublishUnauthorized() {
//        this.webTestClient
//                .post()
//                .uri(PatientResource.SLACK)
//                .body(Mono.just(this.slackPublication), SlackPublication.class)
//                .exchange()
//                .expectStatus().isUnauthorized();
//    }
}
