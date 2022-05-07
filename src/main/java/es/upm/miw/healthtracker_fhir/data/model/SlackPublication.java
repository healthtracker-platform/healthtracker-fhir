package es.upm.miw.healthtracker_fhir.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlackPublication {

    private String title;
    private String name;
    private String slackUsername;
    private String email;
    private SlackPublicationCategory category;
    private String message;

}
