package dev.daw.demo.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    @JsonAlias("user_id")
    @JsonProperty("user_id")
    private int userId;
    @JsonAlias("creation_date")
    @JsonProperty("creation_date")
    private String creationDate;
    @JsonAlias("display_name")
    @JsonProperty("display_name")
    private String displayName;
}
