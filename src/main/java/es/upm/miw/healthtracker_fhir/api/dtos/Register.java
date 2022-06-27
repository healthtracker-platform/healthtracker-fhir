package es.upm.miw.healthtracker_fhir.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Practitioner;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Register {
    private String id;
    private String name;
    private Date date;
    private String type;
    private Integer value;
    private String text;

    public static Register ofObservation(Observation observation) {
        return Register.builder()
                .id(observation.getIdBase())
                .value(observation.getValueQuantity().getValue().intValue())
                .date(observation.getEffectiveDateTimeType().getValue())
                .build();
    }

    public static Register ofEmotionObservation(Observation observation) {
        return Register.builder()
                .id(observation.getIdBase())
                .value(observation.getComponent().get(0).getValueIntegerType().getValue())
                .text(observation.getComponent().get(1).getValueStringType().getValue())
                .date(observation.getEffectiveDateTimeType().getValue())
                .build();
    }
}
