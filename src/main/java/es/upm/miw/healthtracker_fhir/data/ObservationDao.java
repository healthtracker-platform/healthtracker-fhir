package es.upm.miw.healthtracker_fhir.data;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("observationClient")
public class ObservationDao {

    private static final String OBSERVATION = "Observation";
    private static final String SEARCH = "?";
    private static final String CODE = "code=";
    private static final String AND = "&";
    private static final String PATIENT = "subject:Patient.name=";

    private final RepositoryConnector repositoryConnector;

    @Autowired
    public ObservationDao(RepositoryConnector repositoryConnector) {
        this.repositoryConnector = repositoryConnector;
    }

    public String createObservation(Observation observation) {
        return this.repositoryConnector.createResource(observation);
    }

    public Bundle getObservationsByCode(String code, String name) {
        String query = null;
        if(code != null){
            query = OBSERVATION + SEARCH + CODE + code + AND + PATIENT + name.replace(" ","%20");
        }
        return this.repositoryConnector.search(query);
    }
}
