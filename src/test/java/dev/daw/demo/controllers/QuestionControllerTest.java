package dev.daw.demo.controllers;

import dev.daw.demo.exceptions.ApplicationException;
import dev.daw.demo.models.Question;
import dev.daw.demo.models.QuestionDTO;
import dev.daw.demo.services.QuestionService;
import org.json.HTTP;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class QuestionControllerTest {
    @Mock
    QuestionService mockQuestionService;

    @InjectMocks
    QuestionController underTest;

    @Test
    @DisplayName(value = "This test should return success when all questions are retrieved successfully")
    void getQuestions_ShouldReturnSuccess(){
        QuestionDTO questionDTO= new QuestionDTO();
        List<QuestionDTO> expected = Arrays.asList(questionDTO);

        when(mockQuestionService.findAll()).thenReturn(expected);

        // when
        ResponseEntity<List<QuestionDTO>> response = underTest.getQuestions();
        List<QuestionDTO> actual = response.getBody();

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expected.size(), actual.size())
        );
    }

    @Test
    @DisplayName(value = "This test should return success when a question is retrieved successfully")
    void getQuestion_shouldReturnSuccess(){
        QuestionDTO expected = new QuestionDTO();
        expected.setId(1);

        when(mockQuestionService.findQuestion(anyInt())).thenReturn(expected);

        ResponseEntity<QuestionDTO> response = underTest.getQuestion(1);
        QuestionDTO actual = response.getBody();

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expected.getId(), actual.getId())
        );
    }

    @Test
    @DisplayName(value = "This test should return status not found exception when trying to get a nonexistent id")
    void getQuestion_shouldThrowStatusNotFound(){
        when(mockQuestionService.findQuestion(anyInt())).thenReturn(null);

        Exception exception = assertThrows(ApplicationException.class, () -> underTest.getQuestion(1));

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, ((ApplicationException) exception).getHttpStatus()),
                () -> assertEquals("Question with id=1 not found", exception.getMessage())
        );

    }

    @Test
    @DisplayName(value = "This test should return success when trying to delete an question with the specific id")
    void deleteQuestion_shouldDeleteSuccessfully(){

        Integer toDelete = 1;
        when(mockQuestionService.deleteQuestion(anyInt())).thenReturn(toDelete);

        ResponseEntity<Integer> response = underTest.deleteQuestion(1);
        Integer actual = response.getBody();

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(toDelete, actual)
        );
    }


    @Test
    void deleteById_shouldReturnStatusNotFound() {
        when(mockQuestionService.deleteQuestion(anyInt())).thenReturn(null);

        Exception exception = assertThrows(ApplicationException.class, () -> underTest.deleteQuestion(1));

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, ((ApplicationException) exception).getHttpStatus()),
                () -> assertEquals("Could not delete question with id=1; question not found", exception.getMessage())
        );
    }


}
