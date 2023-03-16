package dev.daw.demo.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DateFormatterTest {


    @Test
    @DisplayName("Test returns JsonNode value as iso date string")
    void dateFormatter_shouldReturnString() throws IOException {
        String JsonNodeString = "{\"tags\":[{\"name\":\"postgresql\"},{\"name\":\"prisma\"}],\"owner\":{\"account_id\":84117,\"reputation\":32258,\"user_id\":235334,\"user_type\":\"registered\",\"accept_rate\":91,\"profile_image\":\"https://www.gravatar.com/avatar/6c3b2831a6074aea2c7e3e228f46ea7b?s=256&d=identicon&r=PG\",\"display_name\":\"TruMan1\",\"link\":\"https://stackoverflow.com/users/235334/truman1\"},\"is_answered\":false,\"view_count\":22,\"bounty_amount\":250,\"bounty_closes_date\":1679585361,\"answer_count\":0,\"score\":0,\"last_activity_date\":1678980561,\"creation_date\":1678807214,\"last_edit_date\":1678932898,\"question_id\":75735106,\"content_license\":\"CC BY-SA 4.0\",\"link\":\"https://stackoverflow.com/questions/75735106/prevent-prisma-data-loss-in-production-when-migrate-schema\",\"title\":\"Prevent Prisma data loss in production when migrate schema?\",\"user_id\":235334}\n";
        ObjectMapper om = new ObjectMapper();
        JsonNode expectedNode = om.readTree(JsonNodeString);

        String actual = DateFormatter.formatDate(expectedNode, om);

        assertEquals("2023-03-14T15:20:14Z", actual);

    }

    @Test
    @DisplayName("Test returns returns null as there is no creation_date value in JsonNode")
    void dateFormatter_shouldReturnNull_NoCreationDate() throws IOException {
        String JsonNodeString = "{\"tags\":[{\"name\":\"postgresql\"},{\"name\":\"prisma\"}],\"owner\":{\"account_id\":84117,\"reputation\":32258,\"user_id\":235334,\"user_type\":\"registered\",\"accept_rate\":91,\"profile_image\":\"https://www.gravatar.com/avatar/6c3b2831a6074aea2c7e3e228f46ea7b?s=256&d=identicon&r=PG\",\"display_name\":\"TruMan1\",\"link\":\"https://stackoverflow.com/users/235334/truman1\"},\"is_answered\":false,\"view_count\":22,\"bounty_amount\":250,\"bounty_closes_date\":1679585361,\"answer_count\":0,\"score\":0,\"last_activity_date\":1678980561,\"creation_dates\":1678807214,\"last_edit_date\":1678932898,\"question_id\":75735106,\"content_license\":\"CC BY-SA 4.0\",\"link\":\"https://stackoverflow.com/questions/75735106/prevent-prisma-data-loss-in-production-when-migrate-schema\",\"title\":\"Prevent Prisma data loss in production when migrate schema?\",\"user_id\":235334}\n";
        ObjectMapper om = new ObjectMapper();
        JsonNode expectedNode = om.readTree(JsonNodeString);

        String actual = DateFormatter.formatDate(expectedNode, om);

        assertNull(actual);

    }

    @Test
    @DisplayName("Test returns returns null as the JsonNode is empty")
    void dateFormatter_shouldReturnNull_EmptyNode() throws IOException {
        String JsonNodeString = "{}\n";
        ObjectMapper om = new ObjectMapper();
        JsonNode expectedNode = om.readTree(JsonNodeString);

        String actual = DateFormatter.formatDate(expectedNode, om);

        assertNull(actual);

    }


}
