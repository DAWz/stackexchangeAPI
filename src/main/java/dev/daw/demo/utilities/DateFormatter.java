package dev.daw.demo.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Null;

import java.io.IOException;
import java.time.Instant;

public class DateFormatter {

    public static String formatDate(JsonNode dataNode, ObjectMapper om) throws IOException {
        if(dataNode == null || dataNode.isEmpty() || !dataNode.has("creation_date")) return null;
        JsonNode dateNode = dataNode.get("creation_date");
        Integer dateInt = om.readerFor(new TypeReference<Integer>() {
        }).readValue(dateNode);
        Instant instant = Instant.ofEpochSecond( dateInt );
        String output = instant.toString();
        return output;
    }
}
