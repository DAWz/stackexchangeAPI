package dev.daw.demo.services;

import dev.daw.demo.models.Question;
import dev.daw.demo.models.QuestionDTO;
import dev.daw.demo.models.Tag;
import dev.daw.demo.repositories.QuestionRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @Mock
    QuestionRepo mockQuestionRepo;

    @InjectMocks
    QuestionService underTest;

    @Test
    @DisplayName(value = "This test should return success when all questions are retrieved successfully")
    void getQuestions_shouldReturnQuestionDTOList(){
        // given
        Question question = new Question();
        question.setId(1);
        question.setTags(Collections.singletonList(new Tag("java")));
        List<Question> questionList = Collections.singletonList(question);

        QuestionDTO response = new QuestionDTO();
        response.setId(question.getId());
        List<String> tagStringList =  question.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        response.setTags(tagStringList);

        // mocking findAll method
        when(mockQuestionRepo.findAll()).thenReturn(questionList);

        // when
        List<QuestionDTO> actual = underTest.findAll();


        // then
        assertAll(
                () -> assertEquals(question.getId(), actual.get(0).getId()),
                () -> assertEquals(question.getTags().get(0).getName(), actual.get(0).getTags().get(0))
                );
    }

    @Test
    @DisplayName(value = "This test should return success when all questions are retrieved successfully with NULL tags")
    void getQuestions_shouldReturnQuestionDTOListWithoutTags(){
        Question question = new Question();
        question.setId(1);
        List<Question> questionList = Collections.singletonList(question);

        QuestionDTO response = new QuestionDTO();
        response.setId(question.getId());

        when(mockQuestionRepo.findAll()).thenReturn(questionList);

        List<QuestionDTO> actual = underTest.findAll();

        assertEquals(question.getId(), actual.get(0).getId());
    }
    @Test
    @DisplayName(value = "This test should return empty Question DTO List when there's no questions returned from Repo")
    void getQuestions_shouldReturnEmptyQuestionDTOList(){
        List<Question> questionList = new ArrayList<Question>();

        when(mockQuestionRepo.findAll()).thenReturn(questionList);

        List<QuestionDTO> actual = underTest.findAll();

        assertEquals(questionList.size(), actual.size());
    }

    @Test
    @DisplayName(value = "Test that find question returns the expected QuestionDTO")
    void findQuestion_shouldReturnQuestionDto(){
        Question question = new Question();
        question.setId(1);
        question.setTags(Collections.singletonList(new Tag("java")));

        QuestionDTO response = new QuestionDTO();
        response.setId(question.getId());
        List<String> tagStringList =  question.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        response.setTags(tagStringList);


        when(mockQuestionRepo.findById(anyInt())).
                thenReturn(Optional.of(question));

        QuestionDTO actual = underTest.findQuestion(1);

        assertAll(
                () -> assertEquals(response.getId(), actual.getId()),
                () -> assertEquals(response.getTags(), actual.getTags())
        );

    }

    @Test
    @DisplayName(value = "Test that find question return the question correctly if the tags are empty")
    void findQuestion_shouldReturnQuestionDTOWithNullTags(){
        Question question = new Question();
        question.setId(1);

        QuestionDTO response = new QuestionDTO();
        response.setId(question.getId());

        when(mockQuestionRepo.findById(anyInt())).
                thenReturn(Optional.of(question));

        QuestionDTO actual = underTest.findQuestion(1);

        assertEquals(response.getId(), actual.getId());
    }

    @Test
    @DisplayName(value = "Test that find question returns null when the question is not found")
    void findQuestion_shouldReturnNull_WhenQuestionNotFound(){


        when(mockQuestionRepo.findById(anyInt())).
                thenReturn(Optional.empty());

        QuestionDTO actual = underTest.findQuestion(1);

        assertNull( actual);
    }

    @Test
    @DisplayName(value = "Test that delete question return the expected id of the deleted object")
    void deleteQuestion_shouldReturnId(){
        Question question = new Question();
        question.setId(1);
        when(mockQuestionRepo.findById(anyInt())).thenReturn(Optional.of(question));

        Integer actual = underTest.deleteQuestion(1);

        assertEquals(1, actual);
    }

    @Test
    @DisplayName(value = "Test that delete question returns null when the question is not found")
    void deleteQuestion_shouldReturnNull(){

        when(mockQuestionRepo.findById(anyInt())).thenReturn(Optional.empty());

        Integer actual = underTest.deleteQuestion(1);

        assertNull(actual);
    }

}
