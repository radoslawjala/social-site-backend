package es.com.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {

    private String userName;

    public User() {
    }

    public User(String userName) {
        this.userName = userName;
    }
}
