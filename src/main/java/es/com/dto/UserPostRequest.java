package es.com.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class UserPostRequest {

    @NotBlank
    private String userID;
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public UserPostRequest(String userID, String title, String content) {
        this.userID = userID;
        this.title = title;
        this.content = content;
    }

    public UserPostRequest() {
    }
}
