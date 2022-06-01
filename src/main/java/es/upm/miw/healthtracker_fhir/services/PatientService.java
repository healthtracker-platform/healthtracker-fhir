package es.upm.miw.healthtracker_fhir.services;

import es.upm.miw.healthtracker_fhir.data.PatientMicroserviceRest;
import es.upm.miw.healthtracker_fhir.api.dtos.Patient;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PatientService {

    private final PatientMicroserviceRest patientMicroservice;

    @Autowired
    public PatientService(PatientMicroserviceRest patientMicroservice) {
        this.patientMicroservice = patientMicroservice;
    }

    public Mono<Void> createPatient(Patient patient) {
        org.hl7.fhir.r4.model.Patient fhirPatient = new org.hl7.fhir.r4.model.Patient();
        //First Name and Family Name
        fhirPatient.addName(new HumanName().addGiven(patient.getFirstName()).setFamily(patient.getFamilyName()).setText(patient.getFirstName()+ " " +patient.getFamilyName()));

        //Gender
        if (patient.getGender() == "MALE"){
            fhirPatient.setGender(Enumerations.AdministrativeGender.MALE);
        }else{
            fhirPatient.setGender(Enumerations.AdministrativeGender.FEMALE);
        }

        //E-mail
        fhirPatient.addContact(new org.hl7.fhir.r4.model.Patient.ContactComponent().addTelecom(new ContactPoint().setValue(patient.getEmail())));

        //Reference
        fhirPatient.addGeneralPractitioner(new Reference(patient.getProfessional().replace("http://localhost:8080/fhir","")));

        return this.patientMicroservice.createPatient(fhirPatient);
    }
}
