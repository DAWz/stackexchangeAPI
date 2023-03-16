package dev.daw.demo.controllers;

import dev.daw.demo.exceptions.ApplicationException;
import dev.daw.demo.repositories.QuestionRepo;
import dev.daw.demo.models.QuestionDTO;
import dev.daw.demo.services.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@Validated
public class QuestionController {

    @Autowired
    QuestionService questionService;


    @Operation(summary = "HTTP GET endpoint to get all the questions in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Questions",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No questions found in database", content = @Content)
    })
    @GetMapping({"", "/"})
    public ResponseEntity<List<QuestionDTO>> getQuestions() {
        List<QuestionDTO> questions = questionService.findAll();
            if (questions.isEmpty()) {
                throw new ApplicationException(
                        "no-questions-found",
                        "No questions found in database",
                        HttpStatus.NOT_FOUND
                );
            }
            return new ResponseEntity<>(questions, HttpStatus.OK);

    }

    @Operation(summary = "HTTP GET endpoint to retrieve a single question by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Question By ID",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content= @Content)
    })
    @GetMapping({"/{id}", "/{id}/"})
    @Validated
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable @Positive Integer id) {
        QuestionDTO question = questionService.findQuestion(id);
            if (question == null) {
                throw new ApplicationException(
                        "question-not-found",
                        String.format("Question with id=%d not found", id),
                        HttpStatus.NOT_FOUND
                );
            }
            return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @Operation(summary = "HTTP DELETE endpoint to remove a single question by id from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question deleted"),
    })
    @DeleteMapping({"/{id}", "/{id}/"})
    @Validated
    public ResponseEntity<Integer> deleteQuestion(@PathVariable @Positive Integer id) {
        Integer question = questionService.deleteQuestion(id);
        if (question == null) {
            throw new ApplicationException(
                    "question-not-found",
                    String.format("Could not delete question with id=%d; question not found", id),
                    HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(question, HttpStatus.OK);

    }

    @Operation(summary = "HTTP GET endpoint to retrieve all questions that have specified tags: if operation is 'all' returns all questions with all specified tags, if operation is 'any' returns all questions with any of the specified tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Questions with Tags",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuestionDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No questions found with specified tags", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid operation type provided", content= @Content)
    })
    @GetMapping({"/tagSearch/{tags}", "/tagSearch/{tags}/"})
    @Validated
    public ResponseEntity<List<QuestionDTO>> getQuestionsByTag(@PathVariable("tags") List<String> tags, @RequestParam("operation") @Pattern(regexp = "(?i)(any|all)", message = "operation must be one of any or all") String operation) {
        List<QuestionDTO> questions = questionService.findByTags(tags, tags.size(), operation.equalsIgnoreCase("all"));
        if (questions.isEmpty()) {
            throw new ApplicationException(
                    "no-questions-for-given-tags",
                    String.format("No questions found with %s tags from %s", operation.toLowerCase(), tags),
                    HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }
}
