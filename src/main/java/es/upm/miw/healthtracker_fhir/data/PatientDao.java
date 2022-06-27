package es.upm.miw.healthtracker_fhir.data;


import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("patientClient")
public class PatientDao {

    private static final String PATIENT = "Patient";
    private static final String SEARCH = "?";
    private static final String PROFESSIONAL_NAME = "general-practitioner:Practitioner.name=";
    private static final String NAME = "name=";
    private static final String AND = "&";
    private static final String ACTIVE = "active=true";
    private final RepositoryConnector repositoryConnector;

    @Autowired
    public PatientDao(RepositoryConnector repositoryConnector) {
        this.repositoryConnector = repositoryConnector;
    }


    public String createPatient(Patient patient) {
        return this.repositoryConnector.createResource(patient);
    }

    public Bundle getPatientsByProfessional(String name) {
        String query = null;
        if(name != null){
            query = PATIENT + SEARCH + PROFESSIONAL_NAME + name.replace(" ", "%20") + AND + ACTIVE;
        }else{
            query = PATIENT;
        }
        return this.repositoryConnector.search(query);
    }

    public Bundle getPatientsByNameNullSafe(String name) {
        String query = null;
        if(name != null){
            query = PATIENT + SEARCH + NAME + name.replace(" ", "%20") + AND + ACTIVE;
        }else{
            query = PATIENT + SEARCH + ACTIVE;
        }
        return this.repositoryConnector.search(query);
    }
}
