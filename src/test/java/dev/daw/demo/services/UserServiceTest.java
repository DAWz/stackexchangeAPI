package dev.daw.demo.services;

import dev.daw.demo.exceptions.ApplicationException;
import dev.daw.demo.models.Question;
import dev.daw.demo.models.QuestionDTO;
import dev.daw.demo.models.Tag;
import dev.daw.demo.models.UserDTO;
import dev.daw.demo.repositories.QuestionRepo;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    StackExchangeService mockStackExchangeService;

    @InjectMocks
    UserService underTest;

    @Test
    @DisplayName(value = "This test should successfully return a user")
    void getUser_itShouldReturnUserDTO() throws IOException {
        UserDTO response = new UserDTO();
        response.setUserId(1);

        when(mockStackExchangeService.getUser(1)).
                thenReturn(response);

        UserDTO actual = underTest.getUser(1);

        assertEquals(response.getUserId(), actual.getUserId());
    }

    @Test
    @DisplayName(value = "This test returns null when user is not found")
    void getUser_shouldReturnNull() throws IOException {
        when(mockStackExchangeService.getUser(anyInt())).
                thenReturn(null);

        UserDTO actual = underTest.getUser(1);

        assertNull( actual);

    }

}
