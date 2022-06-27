package es.upm.miw.healthtracker_fhir.api.resources;

import es.upm.miw.healthtracker_fhir.api.Rest;
import es.upm.miw.healthtracker_fhir.api.dtos.Patient;
import es.upm.miw.healthtracker_fhir.api.dtos.Professional;
import es.upm.miw.healthtracker_fhir.api.dtos.Register;
import es.upm.miw.healthtracker_fhir.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Rest
@RequestMapping(RegisterResource.REGISTERS)
public class RegisterResource {

    public static final String REGISTERS = "/registers";
    public static final String WEIGHT = "/weight";
    public static final String WAIST = "/waist";
    public static final String EMOTION = "/emotion";
    public static final String NAME = "/{name}";

    private final RegisterService registerService;

    @Autowired
    public RegisterResource(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping()
    public Mono<String> createRegister(@RequestBody Register register) {
        return this.registerService.createRegister(register);
    }

    @GetMapping(RegisterResource.WEIGHT + NAME)
    public Flux<Register> getWeightRegistersByName(@PathVariable(required = false) String name) {
        return this.registerService.getWeightRegisters(name);
    }
    @GetMapping(RegisterResource.WAIST + NAME)
    public Flux<Register> getWaistRegistersByName(@PathVariable(required = false) String name) {
        return this.registerService.getWaistRegisters(name);
    }

    @GetMapping(RegisterResource.EMOTION + NAME)
    public Flux<Register> getEmotionRegistersByName(@PathVariable(required = false) String name) {
        return this.registerService.getEmotionRegisters(name);
    }

}
