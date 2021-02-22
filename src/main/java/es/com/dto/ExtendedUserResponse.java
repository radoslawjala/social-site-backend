package es.com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtendedUserResponse {

    private String username;
    private String firstname;
    private String lastname;
    private String hobbies;
    private String phoneNumber;
    private String email;
    private byte[] pictureBytes;

    public ExtendedUserResponse(String username, String firstname, String lastname,
                                String hobbies, String phoneNumber, String email, byte[] pictureBytes) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.hobbies = hobbies;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.pictureBytes = pictureBytes;
    }

    public ExtendedUserResponse() {
    }
}
