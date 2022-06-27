package es.upm.miw.healthtracker_fhir.api.resources;

import es.upm.miw.healthtracker_fhir.api.Rest;
import es.upm.miw.healthtracker_fhir.api.dtos.Patient;
import es.upm.miw.healthtracker_fhir.api.dtos.Professional;
import es.upm.miw.healthtracker_fhir.api.dtos.ProfessionalNameDto;
import es.upm.miw.healthtracker_fhir.services.ProfessionalService;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Rest
@RequestMapping(ProfessionalResource.PROFESSIONALS)
public class ProfessionalResource {

    public static final String PROFESSIONALS = "/professionals";
    public static final String SEARCH = "/search";
    public static final String NAME = "/name";
    public static final String ID = "/{id}";

    private final ProfessionalService professionalService;

    @Autowired
    public ProfessionalResource(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @PostMapping()
    public Mono<String> createProfessional(@RequestBody Professional professional) {
        return this.professionalService.createProfessional(professional);
    }

    @GetMapping(SEARCH + NAME)
    public Mono<ProfessionalNameDto> getProfessionalNamesByNameNullSafe(@RequestParam(required = false) String name) {
        return this.professionalService.getProfessionalNamesByNameNullSafe(name);
    }

    @GetMapping(SEARCH)
    public Mono<Professional> getProfessionalByNameNullSafe(@RequestParam(required = false) String name) {
        return this.professionalService.getProfessionalByNameNullSafe(name);
    }

    @GetMapping()
    public Flux<Professional> getProfessionals()
    {
        return this.professionalService.getProfessionals();
    }
}
