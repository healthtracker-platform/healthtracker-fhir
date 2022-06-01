package es.upm.miw.healthtracker_fhir.data;

import org.hl7.fhir.r4.model.Patient;
import es.upm.miw.healthtracker_fhir.services.exceptions.BadGatewayException;
import es.upm.miw.healthtracker_fhir.services.exceptions.ForbiddenException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service("patientClient")
public class PatientMicroserviceRest {
    private final FhirMicroserviceRest fhirMicroserviceRest;

    @Autowired
    public PatientMicroserviceRest(FhirMicroserviceRest azureFhirMicroserviceRest) {
        this.fhirMicroserviceRest = azureFhirMicroserviceRest;
    }


    public Mono<Void> createPatient(Patient patient) {
        return this.fhirMicroserviceRest.postOnFhir("/Patient", patient)
                .flatMap(response -> {
                    if (HttpStatus.UNAUTHORIZED.equals(response.statusCode())) {
                        return Mono.error(new ForbiddenException("Unauthorized Patient Creation"));
                    } else if (response.statusCode().isError()) {
                        return Mono.error(new BadGatewayException("Unexpected error: Patient Microservice. - "
                                            + response.statusCode()));
                    }
                    else {
                        return Mono.empty();
                    }
                });
    }
}
