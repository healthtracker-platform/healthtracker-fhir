package es.upm.miw.healthtracker_fhir.services;

import es.upm.miw.healthtracker_fhir.data.PatientDao;
import es.upm.miw.healthtracker_fhir.api.dtos.Patient;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Service
public class PatientService {

    private final String system;
    private final PatientDao patientDao;

    @Autowired
    public PatientService(PatientDao patientDao, @Value("${miw.azure.resource}") String resource) {
        this.patientDao = patientDao;
        this.system = resource;
    }

    public Mono<String> createPatient(Patient patient) {
        org.hl7.fhir.r4.model.Patient fhirPatient = new org.hl7.fhir.r4.model.Patient();

        //First Name and Family Name
        fhirPatient.addName(new HumanName().addGiven(patient.getFirstName()).setFamily(patient.getFamilyName()).setText(patient.getFirstName()+ " " +patient.getFamilyName()));

        //Gender
        fhirPatient.setGender(Enumerations.AdministrativeGender.valueOf(patient.getGender()));

        //E-mail
        fhirPatient.addContact(new org.hl7.fhir.r4.model.Patient.ContactComponent().addTelecom(new ContactPoint().setValue(patient.getEmail())));

        //Active
        fhirPatient.setActive(true);

        //Reference
        fhirPatient.addGeneralPractitioner(new Reference(patient.getProfessional().replace(this.system,"")));

        String id = this.patientDao.createPatient(fhirPatient);

        return Mono.empty();
    }

    public Flux<Patient> getPatientsByProfessional(String name) {
        List<Patient> list = new ArrayList<>();
        Bundle bundle =  this.patientDao.getPatientsByProfessional(name);
        if (bundle.getEntry().size()>0){
            bundle.getEntry().stream()
                    .forEach(entry->{
                        org.hl7.fhir.r4.model.Patient patient = (org.hl7.fhir.r4.model.Patient) entry.getResource();
                        list.add(Patient.ofPatient(patient));
                    });
        }
        return Mono.just(list).flatMapMany(Flux::fromIterable);
    }

    public Mono<Patient> getPatientByNameNullSafe(String name) {
        List<  org.hl7.fhir.r4.model.Patient> list = new ArrayList<>();
        Bundle bundle = this.patientDao.getPatientsByNameNullSafe(name);
        bundle.getEntry().stream()
                .forEach(entry->{
                    org.hl7.fhir.r4.model.Patient patient = ( org.hl7.fhir.r4.model.Patient) entry.getResource();
                    list.add(patient);
                });
        return Mono.just(Patient.ofPatient(list.get(0)));
    }
}
