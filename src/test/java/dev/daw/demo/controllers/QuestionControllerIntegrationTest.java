package dev.daw.demo.controllers;


import dev.daw.demo.exceptions.ApplicationException;
import dev.daw.demo.models.Question;
import dev.daw.demo.models.QuestionDTO;
import dev.daw.demo.models.Tag;
import dev.daw.demo.repositories.QuestionRepo;
import dev.daw.demo.repositories.TagRepo;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuestionControllerIntegrationTest {

    @Resource
    QuestionRepo questionRepo;
    @Resource
    TagRepo tagRepo;

    @Resource
    QuestionController questionController;


    @BeforeAll
    public void init() {
        questionRepo.deleteAll();
        Tag tag = new Tag("java");
        tagRepo.save(tag);
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        Question question = new Question();
        question.setId(1);
        question.setTags(tags);
        questionRepo.save(question);
    }

    @AfterAll
    public void tear(){
        questionRepo.deleteAll();
    }

    @Test
    @DisplayName(value = "This test checks that the get all questions end point retrieves questions preset for the test")
    public void getQuestions_getOK() {

        ResponseEntity<List<QuestionDTO>> questions = questionController.getQuestions();
        assertAll(()->{
                    assertEquals(1, questions.getBody().size());
                    assertEquals(questions.getStatusCode(), HttpStatus.OK);
                    assertEquals(questions.getBody().size(), questionRepo.findAll().size());
                }
                );

    }

    @Test
    @DisplayName(value = "This test checks that the get question by id endpoint fetches successfully from the test presets")
    public void getQuestionByID_getOK() {

        ResponseEntity<QuestionDTO> question = questionController.getQuestion(1);
        assertAll(()->{
                    assertEquals(1, question.getBody().getId());
                    assertEquals(question.getStatusCode(), HttpStatus.OK);
                    assertEquals(question.getBody().getId(), questionRepo.findById(1).orElse(null).getId());

                }
        );

    }

    @Test
    @DisplayName(value = "Test that get question by id throws an error with invalid path variable")
    public void getQuestionByID_throwValidationError() {

        Exception exception = assertThrows(ConstraintViolationException.class, () -> questionController.getQuestion(-1));

        assertEquals("getQuestion.id: must be greater than 0", exception.getMessage());

    }

    @Test
    @DisplayName(value = "Test the get question by id throws 404 not found")
    public void getQuestionByID_throwNotFoundError() {

        Exception exception = assertThrows(ApplicationException.class, () -> questionController.getQuestion(2));
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, ((ApplicationException) exception).getHttpStatus()),
                () -> assertEquals("Question with id=2 not found", exception.getMessage())
        );

    }

    @Test
    @DisplayName(value = "Test that delete question throws an error with invalid arguments")
    public void deleteQuestionByID_throwValidationError() {

        Exception exception = assertThrows(ConstraintViolationException.class, () -> questionController.getQuestion(-1));

        assertEquals("getQuestion.id: must be greater than 0", exception.getMessage());

    }

    @Test
    @DisplayName(value = "Test that delete question throws a 404 not found")
    public void deleteQuestionByID_throwNotFoundError() {

        Exception exception = assertThrows(ApplicationException.class, () -> questionController.getQuestion(2));
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, ((ApplicationException) exception).getHttpStatus()),
                () -> assertEquals("Question with id=2 not found", exception.getMessage())
        );

    }
    @Test
    @DisplayName(value = "Test that get question by tags retrieves correctly with the operation url parameter set to any")
    public void getQuestionByTags_getOKForAny() {
        List<String> tags = new ArrayList<>();
        tags.add("java");

        ResponseEntity<List<QuestionDTO>> question = questionController.getQuestionsByTag(tags, "any");
        assertAll( ()-> {
                    assertEquals(1, question.getBody().get(0).getId());
                            assertEquals(question.getStatusCode(), HttpStatus.OK);
                            assertEquals(question.getBody().get(0).getId(), questionRepo.findByTagsIn(tags).get(0).getId());

                }
        );
    }

    @Test
    @DisplayName(value = "Test that get question by tags retrieves correctly with the operation url parameter set to all")
    public void getQuestionByTags_getOKForAll() {
        List<String> tags = new ArrayList<>();
        tags.add("java");

        ResponseEntity<List<QuestionDTO>> question = questionController.getQuestionsByTag(tags, "all");
        assertAll( ()-> {
                    assertEquals(1, question.getBody().get(0).getId());
                    assertEquals(question.getStatusCode(), HttpStatus.OK);
                    assertEquals(question.getBody().get(0).getId(), questionRepo.findByAllTags(tags, 1).get(0).getId());

                }
        );
    }

    @Test
    @DisplayName(value = "Test that get questions by tag throws an error with invalid inputs")
    public void getQuestionByTags_getValidationError() {
        List<String> tags = new ArrayList<>();
        tags.add("java");
        Exception exception = assertThrows(ConstraintViolationException.class, () -> questionController.getQuestionsByTag(tags, "and"));

        assertEquals("getQuestionsByTag.operation: operation must be one of any or all", exception.getMessage());
    }
    @Test
    @DisplayName("Test that get questions by tags returns a 404 not found")
    public void getQuestionByTags_getNotFoundError() {
        List<String> tags = new ArrayList<>();
        tags.add("php");
        Exception exception = assertThrows(ApplicationException.class, () -> questionController.getQuestionsByTag(tags, "any"));

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, ((ApplicationException) exception).getHttpStatus()),
                () -> assertEquals("No questions found with any tags from [php]", exception.getMessage())
        );
    }

    @Test
    @DisplayName(value = "Test that delete questions deletes successfully")
    public void deleteQuestionByID_getOK() {

        ResponseEntity<Integer> question = questionController.deleteQuestion(1);
        assertAll(()->{
                    assertEquals(1, question.getBody());
                    assertEquals(question.getStatusCode(), HttpStatus.OK);
                }
        );

    }






}
