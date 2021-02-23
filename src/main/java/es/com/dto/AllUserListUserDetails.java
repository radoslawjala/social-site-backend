package es.com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllUserListUserDetails {

    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private byte[] pictureBytes;

    public AllUserListUserDetails(String id, String username, String firstname, String lastname, byte[] pictureBytes) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.pictureBytes = pictureBytes;
    }

    public AllUserListUserDetails() {
    }
}
