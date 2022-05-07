package es.upm.miw.healthtracker_fhir.api.resources;

import es.upm.miw.healthtracker_fhir.api.Rest;
import es.upm.miw.healthtracker_fhir.data.model.SlackPublication;
import es.upm.miw.healthtracker_fhir.services.SlackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Rest
@RequestMapping(PatientResource.PATIENTS)
public class PatientResource {

    public static final String PATIENTS = "/patients";

    private final SlackService slackService;

    @Autowired
    public PatientResource(SlackService slackService) {
        this.slackService = slackService;
    }

    @PostMapping()
    public Mono<Void> publish(@RequestBody SlackPublication slackPublication) {
        return this.slackService.publish(slackPublication);
    }
}
