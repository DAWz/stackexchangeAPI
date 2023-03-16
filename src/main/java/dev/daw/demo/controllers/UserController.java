package dev.daw.demo.controllers;

import dev.daw.demo.exceptions.ApplicationException;
import dev.daw.demo.models.UserDTO;
import dev.daw.demo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/user")
@Validated
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "HTTP GET endpoint to retrieve a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found User by ID",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Question not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content= @Content)
    })
    @GetMapping({"/{id}", "/{id}/"})
    @Validated
    public ResponseEntity<UserDTO> getUser(@PathVariable @Positive Integer id) throws IOException {
        UserDTO user = userService.getUser(id);
        if(user == null){
            throw new ApplicationException(
                    "user-not-found",
                    String.format("User with id=%d not found", id),
                    HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }
}
