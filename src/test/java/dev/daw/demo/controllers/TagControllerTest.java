package dev.daw.demo.controllers;

import dev.daw.demo.models.Tag;
import dev.daw.demo.services.TagService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.yaml.snakeyaml.tokens.Token.ID.Tag;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TagControllerTest {

    @Mock
    TagService mockTagService;

    @InjectMocks
    TagController underTest;

    @Test
    @DisplayName(value = "This test should return success when all tags are retrieved successfully")
    void getQuestions_ShouldReturnSuccess(){
        Tag tag= new Tag();
        List<Tag> expected = Arrays.asList(tag);

        when(mockTagService.findAll()).thenReturn(expected);

        // when
        ResponseEntity<List<Tag>> response = underTest.getTags();
        List<Tag> actual = response.getBody();

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals(expected.size(), actual.size())
        );
    }
}
