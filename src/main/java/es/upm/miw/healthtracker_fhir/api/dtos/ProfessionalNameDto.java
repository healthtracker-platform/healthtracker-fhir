package es.upm.miw.healthtracker_fhir.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hl7.fhir.r4.model.Practitioner;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfessionalNameDto {
    private List<String> names;

}
