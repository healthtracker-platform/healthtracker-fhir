package es.upm.miw.healthtracker_fhir.services.utils;

import es.upm.miw.healthtracker_fhir.data.model.SlackPublication;
import es.upm.miw.healthtracker_fhir.data.model.SlackPublicationCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class SlackMessageBuilder {

    private static final String MESSAGE_LAYOUT =
        "{" +
            "\"blocks\": [" +
                "{" +
                    "\"type\": \"header\"," +
                    "\"text\": {" +
                    "\"type\": \"plain_text\"," +
                    "\"text\": \"%s %s\"," +
                "}" +
            "}," +
            "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                    "{" +
                    "\"type\": \"mrkdwn\"," +
                    "\"text\": \"*Posted by:*\\n%s\"" +
                    "}," +
                    "{" +
                    "\"type\": \"mrkdwn\"," +
                    "\"text\": \"*Slack username:*\\n%s\"" +
                    "}," +
                    "{" +
                    "\"type\": \"mrkdwn\"," +
                    "\"text\": \"*Email:*\\n%s\"" +
                    "}," +
                    "{" +
                    "\"type\": \"mrkdwn\"," +
                    "\"text\": \"*Category:*\\n%s\"" +
                    "}" +
                "]" +
            "}," +
            "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"%s\"" +
                "}" +
                "]" +
            "}" +
            "]" +
        "}";
    private static final String CLOSE_CASHIER_LAYOUT =
        "{" +
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
                    "\"text\": \"*Date:*\\n%d/%d/%d\"" +
                    "}," +
                    "{" +
                    "\"type\": \"mrkdwn\"," +
                    "\"text\": \"*Time:*\\n%s\"" +
                    "}," +
                    "{" +
                    "\"type\": \"mrkdwn\"," +
                    "\"text\": \"*Card sales:*\\n%s\"" +
                    "}," +
                    "{" +
                    "\"type\": \"mrkdwn\"," +
                    "\"text\": \"*Cash sales:*\\n%s\"" +
                    "}," +
                    "{" +
                    "\"type\": \"mrkdwn\"," +
                    "\"text\": \"*Total:*\\n%s\"" +
                    "}" +
                "]" +
            "}," +
            "{" +
                "\"type\": \"section\"," +
                "\"fields\": [" +
                "{" +
                "\"type\": \"mrkdwn\"," +
                "\"text\": \"%s\"" +
                "}" +
                "]" +
            "}" +
            "]" +
        "}";
    private static final String EUROS = " euros";

    private SlackMessageBuilder() {
        // EMPTY BUT NEEDED
    }

    public static String generateMessage(SlackPublication slackPublication) {
        return String.format(SlackMessageBuilder.MESSAGE_LAYOUT,
                parseCategory(slackPublication).getSymbol(),
                slackPublication.getTitle(),
                slackPublication.getName(),
                slackPublication.getSlackUsername(),
                slackPublication.getEmail(),
                parseCategory(slackPublication).getName(),
                slackPublication.getMessage());
    }


    private static SlackPublicationCategoryText parseCategory(SlackPublication slackPublication) {
        return new SlackPublicationCategoryText(slackPublication.getCategory());
    }

    private static String parseTime(LocalDateTime closureDate) {
        String hour = closureDate.getHour() < 10? "0" + closureDate.getHour() : "" + closureDate.getHour();
        String minute = closureDate.getMinute() < 10? "0" + closureDate.getMinute() : "" + closureDate.getMinute();
        return hour + ":" + minute;
    }

    @Getter
    @Setter
    private static class SlackPublicationCategoryText {

        private String symbol;
        private String name;

        public SlackPublicationCategoryText(SlackPublicationCategory category) {
            switch (category) {
                case INFO:
                    this.symbol = ":information_source:";
                    this.name = "Info";
                    break;
                case WARNING:
                    this.symbol = ":warning:";
                    this.name = "Warning";
                    break;
                case ERROR:
                    this.symbol = ":x:";
                    this.name = "Error";
                    break;
                case CRITICAL:
                    this.symbol = ":rotating_light:";
                    this.name = "Critical";
                    break;
                default:
                    this.symbol = "";
                    this.name = "No category";
            }
        }
    }
}
