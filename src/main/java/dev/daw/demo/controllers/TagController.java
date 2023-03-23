package dev.daw.demo.controllers;


import dev.daw.demo.exceptions.ApplicationException;
import dev.daw.demo.models.Tag;
import dev.daw.demo.services.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/tags")
public class TagController {
    @Autowired
    TagService tagService;

    @Operation(summary = "HTTP GET endpoint to get all the tags in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Questions",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tag.class))}),
            @ApiResponse(responseCode = "404", description = "No questions found in database", content = @Content)
    })
    @GetMapping({"", "/"})
    public ResponseEntity<List<Tag>> getTags() {
        List<Tag> questions = tagService.findAll();
        if (questions.isEmpty()) {
            throw new ApplicationException(
                    "no-tags-found",
                    "No tags found in database",
                    HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(questions, HttpStatus.OK);

    }
}
