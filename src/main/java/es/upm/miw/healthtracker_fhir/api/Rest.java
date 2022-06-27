package es.upm.miw.healthtracker_fhir.api;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)

@PreAuthorize("hasRole('ADMIN') or hasRole('PROFESSIONAL') or hasRole('PATIENT')")
@RestController
@SecurityRequirement(name = "bearerAuth")
public @interface Rest {
}
