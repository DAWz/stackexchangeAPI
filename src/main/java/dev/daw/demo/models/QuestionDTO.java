package dev.daw.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("answer_count")
    private int answerCount;

    @JsonProperty("creation_date")
    private String creationDate;

    @JsonProperty("view_count")
    private int viewCount;

    @JsonProperty("is_answered")
    private boolean isAnswered;

    @JsonProperty("user_id")
    private int userID;

    @JsonProperty("tags")
    private List<String> tags;
}
