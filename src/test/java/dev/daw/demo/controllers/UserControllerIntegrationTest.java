package dev.daw.demo.controllers;


import dev.daw.demo.CodeSampleApplication;
import dev.daw.demo.exceptions.ApplicationException;
import dev.daw.demo.models.QuestionDTO;
import dev.daw.demo.models.UserDTO;
import dev.daw.demo.services.UserService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = CodeSampleApplication.class)
public class UserControllerIntegrationTest {
    @Autowired
    CacheManager cacheManager;

    @Autowired
    UserController userController;

    @BeforeAll
    public void init(){
        cacheManager.getCache("usersCache").clear();
    }
    private Optional<UserDTO> getCachedUser(Integer userID) {
        return ofNullable(cacheManager.getCache("usersCache")).map(c -> c.get(userID, UserDTO.class));
    }

    @Test
    @DisplayName(value = "Test that get user by id caches successfully")
    void getUserID_ResultsCachedSuccessfully() throws IOException {
        ResponseEntity<UserDTO> user = userController.getUser(1);

        assertEquals(user.getBody(), getCachedUser(1).get());
    }

    @Test
    @DisplayName(value = "Test that get user by id retrieves user successfully from the test presets")
    public void getUserById_getOK() throws IOException {

        ResponseEntity<UserDTO> user = userController.getUser(1);
        assertAll(()->{
                    assertEquals(1, user.getBody().getUserId());
                    assertEquals(user.getStatusCode(), HttpStatus.OK);
                }
        );

    }

    @Test
    @DisplayName(value = "Test that get user by id throws an error with invalid inputs")
    public void getQuestionByID_throwValidationError() {

        Exception exception = assertThrows(ConstraintViolationException.class, () -> userController.getUser(-1));

        assertEquals("getUser.id: must be greater than 0", exception.getMessage());

    }

    @Test
    @DisplayName(value = "Test that get user by id returns a 404 not found")
    public void getQuestionByID_throwNotFoundError() {

        Exception exception = assertThrows(ApplicationException.class, () -> userController.getUser(79999999));
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, ((ApplicationException) exception).getHttpStatus()),
                () -> assertEquals("User with id=79999999 not found", exception.getMessage())
        );

    }
}
