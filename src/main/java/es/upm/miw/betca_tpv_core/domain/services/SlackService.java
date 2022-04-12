package es.upm.miw.betca_tpv_core.domain.services;

import es.upm.miw.betca_tpv_core.domain.model.SlackPublication;
import es.upm.miw.betca_tpv_core.domain.rest.SlackMicroservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SlackService {

    private final SlackMicroservice slackMicroservice;

    @Autowired
    public SlackService(SlackMicroservice slackMicroservice) {
        this.slackMicroservice = slackMicroservice;
    }

    public Mono<Void> publish(SlackPublication slackPublication) {
        return this.slackMicroservice.publish(slackPublication);
    }

}
