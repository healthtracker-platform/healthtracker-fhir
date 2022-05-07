package es.upm.miw.healthtracker_fhir.services;

import es.upm.miw.healthtracker_fhir.data.SlackMicroserviceRest;
import es.upm.miw.healthtracker_fhir.data.model.SlackPublication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SlackService {

    private final SlackMicroserviceRest slackMicroservice;

    @Autowired
    public SlackService(SlackMicroserviceRest slackMicroservice) {
        this.slackMicroservice = slackMicroservice;
    }

    public Mono<Void> publish(SlackPublication slackPublication) {
        return this.slackMicroservice.publish(slackPublication);
    }

}
