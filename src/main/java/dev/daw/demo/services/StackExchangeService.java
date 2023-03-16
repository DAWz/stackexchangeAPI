package dev.daw.demo.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.daw.demo.repositories.QuestionRepo;
import dev.daw.demo.repositories.TagRepo;
import dev.daw.demo.models.Question;
import dev.daw.demo.models.Tag;
import dev.daw.demo.models.UserDTO;
import jakarta.annotation.PostConstruct;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;

import org.json.JSONException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.List;


@Component
public class StackExchangeService {
    private final String questionsUrl = "https://api.stackexchange.com/2.3/questions/featured?page=1&pagesize=20&order=desc&sort=creation&site=stackoverflow";

    @Autowired
    QuestionRepo questionRepository;
    @Autowired
    TagRepo tagRepository;
    @PostConstruct
    public void init() throws IOException {
        loadQuestions(questionsUrl);
    }
    public void loadQuestions(String link) throws JSONException, IOException {
        String jsonString = externalCall(link);

        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = om.readTree(jsonString);

        JsonNode internalNode = jsonNode.path("items");
        for (JsonNode tn: internalNode
             ) {

            JsonNode tagsNode = tn.path("tags");
            List<Tag> tagsList = om.readerFor(new TypeReference<List<Tag>>(){}).readValue(tagsNode);
            tagRepository.saveAll(tagsList);
            ArrayNode tagsStringArray = om.valueToTree(tagsList);
            ((ObjectNode)tn).put("tags", tagsStringArray);


            JsonNode ownerNode = tn.path("owner").path("user_id");
            Integer ownerID = om.readerFor(new TypeReference<Integer>() {
            }).readValue(ownerNode);
            ((ObjectNode)tn).put("user_id", ownerID);

            String dateString = formatDate(tn, om);
            ((ObjectNode)tn).put("creation_date", dateString);
        }
        List<Question> questions = om.readerFor(new TypeReference<List<Question>>(){}).readValue(internalNode);
        questionRepository.saveAll(questions);
    }

    public UserDTO getUser(int userId) throws IOException {
        String usersUrl = String.format("https://api.stackexchange.com/2.3/users/%s?order=desc&sort=name&site=stackoverflow", userId);
        String jsonString = externalCall(usersUrl);


        ObjectMapper om = new ObjectMapper();
        JsonNode jsonNode = om.readTree(jsonString);
        JsonNode internalNode = jsonNode.path("items");
        if (internalNode.isEmpty()) return null;
        JsonNode userNode = internalNode.get(0);
        String dateString = formatDate(userNode, om);
        ((ObjectNode) userNode).put("creation_date", dateString);

        UserDTO user = om.readerFor(new TypeReference<UserDTO>() {
        }).readValue(userNode);

        return user;
    }

    public String externalCall(String url) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, String.valueOf(MediaType.APPLICATION_JSON));
        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.GET,
                new HttpEntity<>(headers), String.class);
        String jsonString = response.getBody();
        return jsonString;
    }

    public static String formatDate(JsonNode dataNode, ObjectMapper om) throws IOException {
        JsonNode dateNode = dataNode.get("creation_date");
        Integer dateInt = om.readerFor(new TypeReference<Integer>() {
        }).readValue(dateNode);
        Instant instant = Instant.ofEpochSecond( dateInt );
        String output = instant.toString();
        return output;
    }



}
