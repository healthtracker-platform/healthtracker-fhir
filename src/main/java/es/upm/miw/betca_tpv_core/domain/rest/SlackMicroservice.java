package es.upm.miw.betca_tpv_core.domain.rest;

import es.upm.miw.betca_tpv_core.domain.model.SlackPublication;
import reactor.core.publisher.Mono;

public interface SlackMicroservice {

    Mono<Void> publish(SlackPublication slackPublication);

}
