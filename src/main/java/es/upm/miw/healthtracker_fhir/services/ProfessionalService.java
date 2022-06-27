package es.upm.miw.healthtracker_fhir.services;

import es.upm.miw.healthtracker_fhir.api.dtos.ProfessionalNameDto;
import es.upm.miw.healthtracker_fhir.data.PractitionerDao;
import es.upm.miw.healthtracker_fhir.api.dtos.Professional;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfessionalService {

    private final PractitionerDao professionalDao;

    @Autowired
    public ProfessionalService(PractitionerDao professionalDao) {
        this.professionalDao = professionalDao;
    }

    public Mono<String> createProfessional(Professional professional) {
        org.hl7.fhir.r4.model.Practitioner fhirPractitioner = new org.hl7.fhir.r4.model.Practitioner();

        //First Name and Family Name
        fhirPractitioner.addName(new HumanName().addGiven(professional.getFirstName()).setFamily(professional.getFamilyName()).setText(professional.getFirstName()+ " " +professional.getFamilyName()));

        //Gender
        fhirPractitioner.setGender(Enumerations.AdministrativeGender.valueOf(professional.getGender()));

        //E-mail
        fhirPractitioner.addTelecom(new ContactPoint().setValue(professional.getEmail()));

        //Active
        fhirPractitioner.setActive(true);

        String id = this.professionalDao.createPractitioner(fhirPractitioner);

        return Mono.empty();
    }

    public Mono<ProfessionalNameDto> getProfessionalNamesByNameNullSafe(String name) {
        List<String> list = new ArrayList<>();
        Bundle bundle = this.professionalDao.getPractitionersByNameNullSafe(name);
        bundle.getEntry().stream()
                .forEach(entry->{
                    Practitioner practitioner = (Practitioner)  entry.getResource();
                    list.add(practitioner.getName().get(0).getText());
                });
        return Mono.just(new ProfessionalNameDto(list));
    }

    public Mono<Professional> getProfessionalByNameNullSafe(String name) {
        List<Practitioner> list = new ArrayList<>();
        Bundle bundle = this.professionalDao.getPractitionersByNameNullSafe(name);
        bundle.getEntry().stream()
                .forEach(entry->{
                    Practitioner practitioner = (Practitioner) entry.getResource();
                    list.add(practitioner);
                });
        return Mono.just(Professional.ofPractitioner(list.get(0)));
    }

    public Flux<Professional> getProfessionals() {
        List<Professional> list = new ArrayList<>();
        Bundle bundle =  this.professionalDao.getPractitioners();
        if (bundle.getEntry().size()>0){
            bundle.getEntry().stream()
                    .forEach(entry->{
                        org.hl7.fhir.r4.model.Practitioner practitioner = (org.hl7.fhir.r4.model.Practitioner) entry.getResource();
                        list.add(Professional.ofPractitioner(practitioner));
                    });
        }
        return Mono.just(list).flatMapMany(Flux::fromIterable);
    }

}
