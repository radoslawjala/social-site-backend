package es.com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

//        username: string;
//        firstname: string;
//        lastname: string;
//        hobbies: string;
//        phoneNumber: string;
//        email: string;
public class ExtendedUserResponse {

    private String username;
    private String firstname;
    private String lastname;
    private String hobbies;
    private String phoneNumber;
    private String email;

    public ExtendedUserResponse(String username, String firstname, String lastname,
                                String hobbies, String phoneNumber, String email) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.hobbies = hobbies;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public ExtendedUserResponse() {
    }
}
