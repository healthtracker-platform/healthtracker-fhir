package es.upm.miw.healthtracker_fhir.api.resources;

import es.upm.miw.healthtracker_fhir.api.Rest;
import es.upm.miw.healthtracker_fhir.api.dtos.Patient;
import es.upm.miw.healthtracker_fhir.api.dtos.Professional;
import es.upm.miw.healthtracker_fhir.services.PatientService;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Rest
@RequestMapping(PatientResource.PATIENTS)
public class PatientResource {

    public static final String PATIENTS = "/patients";
    public static final String SEARCH = "/search";

    private final PatientService patientService;

    @Autowired
    public PatientResource(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping()
    public Mono<String> createPatient(@RequestBody Patient patient) {
        return this.patientService.createPatient(patient);
    }

    @GetMapping(SEARCH)
    public Flux<Patient> getPatientsByProfessional(@RequestParam(required = false) String professional) {
        return this.patientService.getPatientsByProfessional(professional);
    }
}
