package dev.daw.demo.utilities;

import dev.daw.demo.exceptions.ApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class CustomHTTPClientTest {

    @Mock
    RestTemplate mockRestTemplate;

    @InjectMocks
    CustomHTTPClient underTest;

    @Test
    @DisplayName("Test returns String from external call response")
    void externalCall_shouldReturnsStringSuccessfully() {
        ResponseEntity<String> expected = new ResponseEntity<String>("ok", HttpStatus.OK);

        when(mockRestTemplate.exchange(  ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(expected);

        String actual = underTest.externalCall("example");

        assertEquals("ok", actual);

    }

    @Test
    @DisplayName("Test throws error after receiving wrong HTTP status from external call")
    void externalCall_shouldThrowError() {
        ResponseEntity<String> expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        when(mockRestTemplate.exchange(  ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(HttpEntity.class),
                ArgumentMatchers.<Class<String>>any())
        ).thenReturn(expected);

        Exception exception = assertThrows(ApplicationException.class, () -> underTest.externalCall("example"));

        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((ApplicationException) exception).getHttpStatus()),
                () -> assertEquals("Failed while calling url: example", exception.getMessage())
        );

    }
}
