package dev.daw.demo.controllers;

import dev.daw.demo.exceptions.ApplicationException;
import dev.daw.demo.models.QuestionDTO;
import dev.daw.demo.models.UserDTO;
import dev.daw.demo.services.UserService;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    UserService mockUserService;

    @InjectMocks
    UserController underTest;

    @DisplayName(value = "This test should return success when a user is retrieved successfully")
    @Test
    void getUser_shouldReturnSuccess() throws IOException {
        UserDTO expected = new UserDTO();
        expected.setUserId(1);

        when(mockUserService.getUser(1)).thenReturn(expected);

        ResponseEntity<UserDTO> response = underTest.getUser(1);
        UserDTO actual = response.getBody();

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expected.getUserId(), actual.getUserId())
        );
    }
    @DisplayName(value = "This test should fail throwing user not found")
    @Test
    void getUser_shouldReturnNotFound() throws IOException {
        when(mockUserService.getUser(anyInt())).thenReturn(null);

        Exception exception = assertThrows(ApplicationException.class, () -> underTest.getUser(1));

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, ((ApplicationException) exception).getHttpStatus()),
                () -> assertEquals("User with id=1 not found", exception.getMessage())
        );
    }
}
