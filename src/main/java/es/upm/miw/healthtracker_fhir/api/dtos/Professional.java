package es.upm.miw.healthtracker_fhir.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Practitioner;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Professional {
    private String id;
    private String email;
    private String firstName;
    private String familyName;
    private String gender;


    public static Professional ofPractitioner(Practitioner practitioner) {
        return Professional.builder()
                .id(practitioner.getIdBase())
                .email(practitioner.getTelecom().get(0).getValue())
                .firstName(practitioner.getName().get(0).getGivenAsSingleString())
                .familyName(practitioner.getName().get(0).getFamily())
                .gender(practitioner.getGenderElement().toString())
                .build();
    }
}
