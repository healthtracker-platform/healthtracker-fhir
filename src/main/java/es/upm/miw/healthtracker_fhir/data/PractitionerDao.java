package es.upm.miw.healthtracker_fhir.data;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("professionalClient")
public class PractitionerDao {
    private static final String PRACTITIONER = "Practitioner";
    private static final String SEARCH = "?";
    private static final String NAME = "name=";
    private static final String AND = "&";
    private static final String ACTIVE = "active=true";
    private final RepositoryConnector repositoryConnector;

    @Autowired
    public PractitionerDao(RepositoryConnector repositoryConnector) {
        this.repositoryConnector = repositoryConnector;
    }

    public String createPractitioner(Practitioner practitioner) {
        return this.repositoryConnector.createResource(practitioner);
    }

    public Bundle getPractitionersByNameNullSafe(String name) {
        String query = null;
        if(name != null){
            query = PRACTITIONER + SEARCH + NAME + name.replace(" ", "%20") + AND + ACTIVE;
        }else{
            query = PRACTITIONER + SEARCH + ACTIVE;
        }
        return this.repositoryConnector.search(query);
    }

    public Bundle getPractitioners() {
        String query = PRACTITIONER + SEARCH + ACTIVE;
        return this.repositoryConnector.search(query);
    }
}
