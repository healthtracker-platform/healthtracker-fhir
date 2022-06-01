package es.upm.miw.healthtracker_fhir.services;

import ca.uhn.fhir.context.FhirContext;
import es.upm.miw.healthtracker_fhir.api.dtos.ProfessionalNameDto;
import es.upm.miw.healthtracker_fhir.data.ProfessionalMicroserviceRest;
import es.upm.miw.healthtracker_fhir.api.dtos.Professional;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfessionalService {

    private final ProfessionalMicroserviceRest professionalMicroservice;

    @Autowired
    public ProfessionalService(ProfessionalMicroserviceRest professionalMicroservice) {
        this.professionalMicroservice = professionalMicroservice;
    }

    public Mono<Void> createProfessional(Professional professional) {
        org.hl7.fhir.r4.model.Practitioner fhirPractitioner = new org.hl7.fhir.r4.model.Practitioner();
        //First Name and Family Name
        fhirPractitioner.addName(new HumanName().addGiven(professional.getFirstName()).setFamily(professional.getFamilyName()).setText(professional.getFirstName()+ " " +professional.getFamilyName()));

        //Gender
        fhirPractitioner.setGender(Enumerations.AdministrativeGender.valueOf(professional.getGender()));

        //E-mail
        fhirPractitioner.addTelecom(new ContactPoint().setValue(professional.getEmail()));

        return this.professionalMicroservice.createProfessional(fhirPractitioner);
    }


    public Mono<ProfessionalNameDto> getProfessionalNamesByNameNullSafe(String name) {

      return this.professionalMicroservice.getProfessionalsByNameNullSafe(name)
                .map(bundleString-> {
                    List list = new ArrayList<String>();
                   Bundle bundle = FhirContext.forR4().newJsonParser().parseResource(Bundle.class, bundleString);
                   bundle.getEntry().stream().forEach(entry->{
                      Practitioner practitioner = (Practitioner) entry.getResource();
                     list.add(practitioner.getName().get(0).getText());
                   });
                   return new ProfessionalNameDto(list);
                });
    }

    public Mono<Professional> getProfessionalByNameNullSafe(String name) {
        return this.professionalMicroservice.getProfessionalsByNameNullSafe(name)
                .map(bundleString-> {
                    List<Practitioner> list = new ArrayList<>();
                    Bundle bundle = FhirContext.forR4().newJsonParser().parseResource(Bundle.class, bundleString);
                    bundle.getEntry().stream().forEach(entry->{
                        Practitioner practitioner = (Practitioner) entry.getResource();
                        list.add(practitioner);
                    });
                    return Professional.ofPractitioner(list.get(0));
                });
    }
}
