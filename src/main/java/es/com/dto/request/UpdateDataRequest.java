package es.com.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class UpdateDataRequest {

    @NotBlank
    private String id;
    @NotBlank
    @Size(min = 3, max = 20)
    private String firstname;
    @NotBlank
    @Size(min = 3, max = 20)
    private String lastname;
    @NotBlank
    private String hobbies;
    @NotBlank
    private String phoneNumber;

    public UpdateDataRequest(String firstname, String lastname, String hobbies, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.hobbies = hobbies;
        this.phoneNumber = phoneNumber;
    }

    public UpdateDataRequest() {
    }
}
