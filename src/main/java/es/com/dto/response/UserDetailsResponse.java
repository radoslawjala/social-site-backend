package es.com.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsResponse {

    private String username;
    private String firstname;
    private String lastname;
    private String hobbies;
    private String phoneNumber;
    private byte[] pictureBytes;

    public UserDetailsResponse(String username, String firstname, String lastname, String hobbies, String phoneNumber, byte[] pictureBytes) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.hobbies = hobbies;
        this.phoneNumber = phoneNumber;
        this.pictureBytes = pictureBytes;
    }

    public UserDetailsResponse() {
    }
}
