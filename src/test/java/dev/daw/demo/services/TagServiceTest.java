package dev.daw.demo.services;

import dev.daw.demo.models.Tag;
import dev.daw.demo.repositories.TagRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @Mock
    TagRepo mockTagRepo;

    @InjectMocks
    TagService underTest;

    @Test
    @DisplayName(value = "This test should return success when all tags are retrieved successfully")
    void getTags_shouldReturnTagsList(){
        Tag tag = new Tag();
        tag.setName("test");
        List<Tag> tags = Collections.singletonList(tag);

        when(mockTagRepo.findAll()).thenReturn(tags);
        List<Tag> actual = underTest.findAll();

        assertAll(
                () -> assertEquals(tag.getName(), actual.get(0).getName())
        );
    }
}
