package es.upm.miw.healthtracker_fhir.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hl7.fhir.r4.model.Practitioner;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Patient {

    private String id;
    private String email;
    private String firstName;
    private String familyName;
    private String gender;
    private String professional;

    public static Patient ofPatient(org.hl7.fhir.r4.model.Patient patient) {
        String email;
        String firstName;
        String familyName;
        String gender;
        String professional;


        return Patient.builder()
                .id(patient.getId())
                .email(patient.getContact().get(0).getTelecom().get(0).getValue())
                .firstName(patient.getName().get(0).getGivenAsSingleString())
                .familyName(patient.getName().get(0).getFamily())
                .gender(patient.getGenderElement().getCode())
                .build();
    }
}
