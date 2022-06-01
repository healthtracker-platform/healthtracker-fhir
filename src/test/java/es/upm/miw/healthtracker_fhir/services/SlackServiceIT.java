package es.upm.miw.healthtracker_fhir.services;

import es.upm.miw.healthtracker_fhir.TestConfig;
import es.upm.miw.healthtracker_fhir.api.dtos.Patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;

@TestConfig
class SlackServiceIT {

    @Autowired
    private PatientService slackService;
    @MockBean
    private PatientService slackMicroservice;

    private Patient slackPublication;

//    @BeforeEach
//    void setUp() {
//        this.slackPublication = new Patient("titulo", "autor", "username",
//                "email", SlackPublicationCategory.CRITICAL, "message");
//
//        BDDMockito.given(this.slackMicroservice.publish(any(Patient.class)))
//                .willAnswer(arguments -> Mono.empty());
//
//    }
//
//    @Test
//    void testPublish() {
//        StepVerifier
//                .create(this.slackService.publish(this.slackPublication))
//                .verifyComplete();
//    }


}
