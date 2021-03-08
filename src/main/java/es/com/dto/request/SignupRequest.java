package es.com.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@ToString
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 3, max = 20)
    private String firstname;

    @NotBlank
    @Size(min = 3, max = 20)
    private String lastname;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Email
    @Size(min = 3, max = 50)
    private String email;

    @NotBlank
    private String dateOfBirth;

    @NotBlank
    @Size(max = 30)
    private String city;

    @NotBlank
    @Size(max = 50)
    private String hobbies;

    @NotBlank
    private String phoneNumber;

    private Set<String> role;

    public SignupRequest(String username, String firstname, String lastname,
                         String password, String email, String dateOfBirth,
                         String city, String hobbies, String phoneNumber) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.hobbies = hobbies;
        this.phoneNumber = phoneNumber;
    }

    public SignupRequest() {
    }
}
