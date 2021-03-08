package es.com.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangePasswordRequest {

    private String oldPassword;
    private String newPassword;
    private String userID;

    public ChangePasswordRequest(String oldPassword, String newPassword, String userID) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.userID = userID;
    }

    public ChangePasswordRequest() {
    }
}
