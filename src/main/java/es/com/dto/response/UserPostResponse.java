package es.com.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPostResponse {

    private String title;
    private String content;
    private String date;

    public UserPostResponse(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public UserPostResponse() {
    }
}
