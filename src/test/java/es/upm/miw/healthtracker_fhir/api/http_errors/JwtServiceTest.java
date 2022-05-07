package es.upm.miw.healthtracker_fhir.api.http_errors;

import es.upm.miw.healthtracker_fhir.TestConfig;
import es.upm.miw.healthtracker_fhir.configuration.JwtService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestConfig
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

  //  @Test
  //  void testCreateToken() {
       // String token = jwtService.createToken("666666000", "adm", Role.ADMIN.name());
       // assertFalse(token.isEmpty());
       // assertEquals(3, token.split("\\.").length);
   // }
}
