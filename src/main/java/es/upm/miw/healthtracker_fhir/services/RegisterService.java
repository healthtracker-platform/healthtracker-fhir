package es.upm.miw.healthtracker_fhir.services;
import es.upm.miw.healthtracker_fhir.api.dtos.Patient;
import es.upm.miw.healthtracker_fhir.api.dtos.Register;
import es.upm.miw.healthtracker_fhir.data.ObservationDao;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RegisterService {

    public static final String WEIGHT_CODE = "726527001";
    public static final String WAIST_DIAMETER_CODE = "276361009";
    public static final String EMOTION_CODE = "285854004";
    private final String system;
    private final ObservationDao obervationDao;
    private final PatientService patientService;

    @Autowired
    public RegisterService(ObservationDao obervationDao, PatientService patientService, @Value("${miw.azure.resource}") String resource ) {
        this.obervationDao = obervationDao;
        this.patientService = patientService;
        this.system = resource;
    }

    public Mono<String> createRegister(Register register) {
        Patient patient = this.patientService.getPatientByNameNullSafe(register.getName()).block();
        org.hl7.fhir.r4.model.Observation observation = new org.hl7.fhir.r4.model.Observation();
        observation.setStatus(Observation.ObservationStatus.FINAL);
        observation.setSubject(new Reference(patient.getId().replace(this.system,"")));
        observation.setEffective(new DateTimeType(new Date()));
        if(register.getType().equals("weight")){
            Coding codeWeight = new Coding().setSystem("http://snomed.info/sct").setCode(WEIGHT_CODE).setDisplay("Weight");
            observation.setCode(new CodeableConcept().addCoding(codeWeight));

            Quantity quantityWeigth = new Quantity().setSystem("http://snomed.info/sct").setCode("258683005").setUnit("Kilogram").setValue(register.getValue());
            observation.setValue(quantityWeigth);
        }
        if(register.getType().equals("waist-diameter")){
            Coding codeWaist = new Coding().setSystem("http://snomed.info/sct").setCode(WAIST_DIAMETER_CODE).setDisplay("Waist circumference");
            observation.setCode(new CodeableConcept().addCoding(codeWaist));

            Quantity quantityWaist = new Quantity().setSystem("http://snomed.info/sct").setCode("258672001").setUnit("Centimeter").setValue(register.getValue());
            observation.setValue(quantityWaist);
        }
        if(register.getType().equals("emotion")){
            Coding codeEmotion = new Coding().setSystem("http://snomed.info/sct").setCode(EMOTION_CODE).setDisplay("Emotion");
            observation.setCode(new CodeableConcept().addCoding(codeEmotion));

            Coding codeIntensity = new Coding().setSystem("http://snomed.info/sct").setCode("1193646009").setDisplay("Intensity of emotion");
            observation.addComponent(new Observation.ObservationComponentComponent().setCode(new CodeableConcept().addCoding(codeIntensity)).setValue(new IntegerType (register.getValue())));

            Coding codeClarification = new Coding().setSystem("http://snomed.info/sct").setCode("438589007").setDisplay("Clarification of emotion");
            observation.addComponent(new Observation.ObservationComponentComponent().setCode(new CodeableConcept().addCoding(codeClarification)).setValue(new StringType(register.getText())));
        }
        String id = this.obervationDao.createObservation(observation);

        return Mono.empty();
    }

    public Flux<Register> getWeightRegisters(String name) {
        List<Register> list = new ArrayList<>();
        Bundle bundle =  this.obervationDao.getObservationsByCode(WEIGHT_CODE, name);
        if (bundle.getEntry().size()>0){
            bundle.getEntry().stream()
                    .forEach(entry->{
                        org.hl7.fhir.r4.model.Observation observation = (org.hl7.fhir.r4.model.Observation) entry.getResource();
                        list.add(Register.ofObservation(observation));
                    });
        }
        return Mono.just(list).flatMapMany(Flux::fromIterable);
    }

    public Flux<Register> getWaistRegisters(String name) {
        List<Register> list = new ArrayList<>();
        Bundle bundle =  this.obervationDao.getObservationsByCode(WAIST_DIAMETER_CODE, name);
        if (bundle.getEntry().size()>0){
            bundle.getEntry().stream()
                    .forEach(entry->{
                        org.hl7.fhir.r4.model.Observation observation = (org.hl7.fhir.r4.model.Observation) entry.getResource();
                        list.add(Register.ofObservation(observation));
                    });
        }
        return Mono.just(list).flatMapMany(Flux::fromIterable);
    }

    public Flux<Register> getEmotionRegisters(String name) {
        List<Register> list = new ArrayList<>();
        Bundle bundle =  this.obervationDao.getObservationsByCode(EMOTION_CODE, name);
        if (bundle.getEntry().size()>0){
            bundle.getEntry().stream()
                    .forEach(entry->{
                        org.hl7.fhir.r4.model.Observation observation = (org.hl7.fhir.r4.model.Observation) entry.getResource();
                        list.add(Register.ofEmotionObservation(observation));
                    });
        }
        return Mono.just(list).flatMapMany(Flux::fromIterable);
    }
}
