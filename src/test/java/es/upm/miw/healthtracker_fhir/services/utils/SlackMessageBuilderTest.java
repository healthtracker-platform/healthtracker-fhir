package es.upm.miw.healthtracker_fhir.services.utils;

import es.upm.miw.healthtracker_fhir.data.model.SlackPublication;
import es.upm.miw.healthtracker_fhir.data.model.SlackPublicationCategory;
import es.upm.miw.healthtracker_fhir.services.utils.SlackMessageBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class SlackMessageBuilderTest {

    @Test
    void testGenerateMessageCritical() {
        SlackPublication slackPublication = new SlackPublication("titulo", "autor", "username",
                "email", SlackPublicationCategory.CRITICAL, "elcontenido");
        String expectedAnswer = "{" +
                "\"blocks\": [" +
                "{" +
                "\"type\": \"header\"," +
                "\"text\": {" +
                "\"type\": \"plain_text\"," +
                "\"text\": \":rotating_light: titulo\"," +
                "}" +
                "}," +
                "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Posted by:*\\nautor\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Slack username:*\\nusername\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Email:*\\nemail\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Category:*\\nCritical\"" +
                "}" +
                "]" +
                "}," +
                "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"elcontenido\"" +
                "}" +
                "]" +
                "}" +
                "]" +
                "}";
        Assertions.assertEquals(expectedAnswer, SlackMessageBuilder.generateMessage(slackPublication));
    }

    @Test
    void testGenerateMessageError() {
        SlackPublication slackPublication = new SlackPublication("titulo", "autor", "username",
                "email", SlackPublicationCategory.ERROR, "elcontenido");
        String expectedAnswer = "{" +
                "\"blocks\": [" +
                "{" +
                "\"type\": \"header\"," +
                "\"text\": {" +
                "\"type\": \"plain_text\"," +
                "\"text\": \":x: titulo\"," +
                "}" +
                "}," +
                "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Posted by:*\\nautor\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Slack username:*\\nusername\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Email:*\\nemail\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Category:*\\nError\"" +
                "}" +
                "]" +
                "}," +
                "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"elcontenido\"" +
                "}" +
                "]" +
                "}" +
                "]" +
                "}";
        Assertions.assertEquals(expectedAnswer, SlackMessageBuilder.generateMessage(slackPublication));
    }

    @Test
    void testGenerateMessageInfo() {
        SlackPublication slackPublication = new SlackPublication("titulo", "autor", "username",
                "email", SlackPublicationCategory.INFO, "elcontenido");
        String expectedAnswer = "{" +
                "\"blocks\": [" +
                "{" +
                "\"type\": \"header\"," +
                "\"text\": {" +
                "\"type\": \"plain_text\"," +
                "\"text\": \":information_source: titulo\"," +
                "}" +
                "}," +
                "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Posted by:*\\nautor\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Slack username:*\\nusername\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Email:*\\nemail\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Category:*\\nInfo\"" +
                "}" +
                "]" +
                "}," +
                "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"elcontenido\"" +
                "}" +
                "]" +
                "}" +
                "]" +
                "}";
        Assertions.assertEquals(expectedAnswer, SlackMessageBuilder.generateMessage(slackPublication));
    }

    @Test
    void testGenerateMessageWarning() {
        SlackPublication slackPublication = new SlackPublication("titulo", "autor", "username",
                "email", SlackPublicationCategory.WARNING, "elcontenido");
        String expectedAnswer = "{" +
                "\"blocks\": [" +
                "{" +
                "\"type\": \"header\"," +
                "\"text\": {" +
                "\"type\": \"plain_text\"," +
                "\"text\": \":warning: titulo\"," +
                "}" +
                "}," +
                "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Posted by:*\\nautor\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Slack username:*\\nusername\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Email:*\\nemail\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Category:*\\nWarning\"" +
                "}" +
                "]" +
                "}," +
                "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"elcontenido\"" +
                "}" +
                "]" +
                "}" +
                "]" +
                "}";
        Assertions.assertEquals(expectedAnswer, SlackMessageBuilder.generateMessage(slackPublication));
    }

    @Test
    void testCloseCashierMinutesLessThanTen() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 3, 21, 17, 6);

        String expectedAnswer = "{" +
                "\"blocks\": [" +
                "{" +
                "\"type\": \"header\"," +
                "\"text\": {" +
                "\"type\": \"plain_text\"," +
                "\"text\": \":receipt: Cashier closure\"," +
                "}" +
                "}," +
                "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Date:*\\n21/3/2022\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Time:*\\n17:06\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Card sales:*\\n79 euros\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Cash sales:*\\n100 euros\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Total:*\\n179 euros\"" +
                "}" +
                "]" +
                "}," +
                "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"Comentario\"" +
                "}" +
                "]" +
                "}" +
                "]" +
                "}";

    }

    @Test
    void testCloseCashierHourLessThanTen() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 3, 21, 7, 36);

        String expectedAnswer = "{" +
                "\"blocks\": [" +
                "{" +
                "\"type\": \"header\"," +
                "\"text\": {" +
                "\"type\": \"plain_text\"," +
                "\"text\": \":receipt: Cashier closure\"," +
                "}" +
                "}," +
                "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Date:*\\n21/3/2022\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Time:*\\n07:36\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Card sales:*\\n79 euros\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Cash sales:*\\n100 euros\"" +
                "}," +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"*Total:*\\n179 euros\"" +
                "}" +
                "]" +
                "}," +
                "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"Comentario\"" +
                "}" +
                "]" +
                "}" +
                "]" +
                "}";

    }
}
