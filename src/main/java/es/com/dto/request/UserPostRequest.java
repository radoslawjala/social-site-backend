package es.com.dto.request;

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
    @NotBlank
    private String date;

    public UserPostRequest(String userID, String title, String content, String date) {
        this.userID = userID;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public UserPostRequest() {
    }
}
