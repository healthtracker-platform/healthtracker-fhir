package es.upm.miw.healthtracker_fhir.data;

import es.upm.miw.healthtracker_fhir.api.dtos.Professional;
import es.upm.miw.healthtracker_fhir.services.exceptions.BadGatewayException;
import es.upm.miw.healthtracker_fhir.services.exceptions.ForbiddenException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("professionalClient")
public class ProfessionalMicroserviceRest {
    private final FhirMicroserviceRest fhirMicroserviceRest;

    @Autowired
    public ProfessionalMicroserviceRest(FhirMicroserviceRest fhirMicroserviceRest) {
        this.fhirMicroserviceRest = fhirMicroserviceRest;
    }


    public Mono<Void> createProfessional(Practitioner practitioner) {
        return this.fhirMicroserviceRest.postOnFhir("/Practitioner", practitioner)
                .flatMap(response -> {
                    if (HttpStatus.UNAUTHORIZED.equals(response.statusCode())) {
                        return Mono.error(new ForbiddenException("Unauthorized Practitioner Creation"));
                    } else if (response.statusCode().isError()) {
                        return Mono.error(new BadGatewayException("Unexpected error: Practitioner Microservice. - "
                                + response.statusCode()));
                    }
                    else {
                        return Mono.empty();
                    }
                });
    }

    public Mono<String> getProfessionalsByNameNullSafe(String name) {
        String uri = null;
        if(name==null){
            uri = "/Practitioner";
        }else{
            uri = "/Practitioner?name="+name;
        }
        return this.fhirMicroserviceRest.getOnFhir(uri)
                .flatMap(response -> {
                    if (HttpStatus.UNAUTHORIZED.equals(response.statusCode())) {
                        return Mono.error(new ForbiddenException("Unauthorized Practitioner Search"));
                    } else if (response.statusCode().isError()) {
                        return Mono.error(new BadGatewayException("Unexpected error: Practitioner Microservice. - "
                                + response.statusCode()));
                    }
                    else {
                        return response.bodyToMono(String.class);
                    }

                });
    }
}
