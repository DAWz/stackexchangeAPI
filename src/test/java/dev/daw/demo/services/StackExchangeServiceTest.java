package dev.daw.demo.services;

import dev.daw.demo.models.Question;
import dev.daw.demo.models.Tag;
import dev.daw.demo.models.UserDTO;
import dev.daw.demo.repositories.QuestionRepo;
import dev.daw.demo.repositories.TagRepo;
import dev.daw.demo.utilities.CustomHTTPClient;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class StackExchangeServiceTest {
    @Mock
    QuestionRepo mockQuestionRepo;
    @Mock
    TagRepo mockTagRepo;

    @Mock
    CustomHTTPClient mockHTTPClient;

    @InjectMocks
    StackExchangeService underTest;



    @Test
    @DisplayName(value = "This test should return success when all questions are added successfully")
    void getQuestions_shouldReturnQuestionList() throws JSONException, IOException {
        String successfulExternalResponse = "{\"items\":[{\"tags\":[\"java\"],\"owner\":{\"account_id\":13780227,\"reputation\":1329,\"user_id\":9945524,\"user_type\":\"registered\",\"profile_image\":\"https://www.gravatar.com/avatar/62eaf2fcc2b025e175f21702d06ace87?s=256&d=identicon&r=PG\",\"display_name\":\"Mark Cilia Vincenti\",\"link\":\"https://stackoverflow.com/users/9945524/mark-cilia-vincenti\"},\"is_answered\":false,\"view_count\":12,\"bounty_amount\":50,\"bounty_closes_date\":1679590821,\"answer_count\":0,\"score\":0,\"last_activity_date\":1678986021,\"creation_date\":1678810117,\"question_id\":1,\"content_license\":\"CC BY-SA 4.0\",\"link\":\"https://stackoverflow.com/questions/75735676/azure-pipeline-gulp-yarn-install-fails-authorization-on-private-repo\",\"title\":\"Azure pipeline gulp yarn install fails authorization on private repo\"}],\"has_more\":true,\"quota_max\":300,\"quota_remaining\":280}\n";

        Question question = new Question();
        question.setId(1);
        List<Tag> exampleTagList = new ArrayList<Tag>();
        exampleTagList.add(new Tag("java"));
        question.setTags(exampleTagList);
        List<Question> questionList = Collections.singletonList(question);

        when(mockHTTPClient.externalCall(anyString())).thenReturn(successfulExternalResponse);
        when(mockTagRepo.saveAll(anyList())).thenReturn(exampleTagList);
        when(mockQuestionRepo.saveAll(anyList())).thenReturn(questionList);

        List<Question> actual = underTest.loadQuestions("example");

        assertAll(
                () -> assertEquals(question.getId(), actual.get(0).getId()),
                () -> assertEquals(question.getTags().get(0).getName(), actual.get(0).getTags().get(0).getName()),
                ()-> assertEquals(questionList.size(), actual.size())
        );
    }

    @Test
    @DisplayName(value = "This test should return null if empty response array")
    void getQuestions_shouldReturnNull() throws JSONException, IOException {
        String emptyExternalResponse = "{\"items\":[],\"has_more\":true,\"quota_max\":300,\"quota_remaining\":280}\n";

        when(mockHTTPClient.externalCall(anyString())).thenReturn(emptyExternalResponse);

        List<Question> actual = underTest.loadQuestions("example");

        assertNull(actual);
    }

    @Test
    @DisplayName(value = "This test should successfully return a user")
    void getUser_shouldReturnUser() throws IOException {
        String userResponse = "{\"items\":[{\"badge_counts\":{\"bronze\":153,\"silver\":149,\"gold\":48},\"account_id\":1,\"is_employee\":false,\"last_modified_date\":1677542701,\"last_access_date\":1678561454,\"reputation_change_year\":130,\"reputation_change_quarter\":130,\"reputation_change_month\":20,\"reputation_change_week\":0,\"reputation_change_day\":0,\"reputation\":63051,\"creation_date\":1217514151,\"user_type\":\"registered\",\"user_id\":1,\"accept_rate\":100,\"location\":\"El Cerrito, CA\",\"website_url\":\"https://blog.codinghorror.com/\",\"link\":\"https://stackoverflow.com/users/1/jeff-atwood\",\"profile_image\":\"https://www.gravatar.com/avatar/51d623f33f8b83095db84ff35e15dbe8?s=256&d=identicon&r=PG\",\"display_name\":\"Jeff Atwood\"}],\"has_more\":false,\"quota_max\":300,\"quota_remaining\":267}\n";

        UserDTO expectedUser = new UserDTO();
        expectedUser.setUserId(1);
        expectedUser.setCreationDate("2008-07-31T14:22:31Z");
        expectedUser.setDisplayName("Jeff Atwood");

        when(mockHTTPClient.externalCall(anyString())).thenReturn(userResponse);
        UserDTO actual = underTest.getUser(1);

        assertAll(
                () -> assertEquals(expectedUser.getCreationDate(), actual.getCreationDate()),
                () -> assertEquals(expectedUser.getDisplayName(), actual.getDisplayName()),
                ()-> assertEquals(expectedUser.getUserId(), actual.getUserId())
        );
    }

    @Test
    @DisplayName(value = "This test should return null if empty response json")
    void getUser_shouldReturnNull() throws IOException {
        String emptyExternalResponse = "{\"items\":[],\"has_more\":true,\"quota_max\":300,\"quota_remaining\":280}\n";

        when(mockHTTPClient.externalCall(anyString())).thenReturn(emptyExternalResponse);

        UserDTO actual = underTest.getUser(1);

        assertNull(actual);
    }
}
