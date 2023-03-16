package dev.daw.demo.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "questions")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {


    @Column(name = "id")
    @Getter
    @Setter
    @JsonAlias("question_id")
    @Id
    private int id;



    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "tag_question",
            joinColumns = @JoinColumn(name = "question_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_name",
                    referencedColumnName = "name"))
    @Getter
    @Setter
    @JsonAlias("tags")
    private List<Tag> tags;

    @Column(name = "is_answered")
    @Getter
    @Setter
    @JsonAlias("is_answered")
    private boolean isAnswered;
    @Column(name = "view_count")
    @Getter
    @Setter
    @JsonAlias("viewCount")
    private int viewCount;
    @Column(name = "answer_count")
    @Getter
    @Setter
    @JsonAlias("answers_count")
    private int answerCount;
    @Column(name = "creation_date")
    @Getter
    @Setter
    @JsonAlias("creation_date")
    private String creationDate;
    @Getter
    @Setter
    @JsonAlias("user_id")
    private int userID;


    public static QuestionDTO formatQuestion(Question q){
        if(q == null) return null;
        List<String> tags = q.getTags() == null ? null : q.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        QuestionDTO questionDTO = new QuestionDTO(q.getId(), q.getAnswerCount(), q.getCreationDate(), q.getViewCount(), q.isAnswered(), q.getUserID(), tags);
        return questionDTO;
    }
}
