package es.com.dto;

public class UserDetailsResponse {

    private String firstname;
    private String lastname;
    private String hobbies;
    private String phoneNumber;

    public UserDetailsResponse(String firstname, String lastname, String hobbies, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.hobbies = hobbies;
        this.phoneNumber = phoneNumber;
    }

    public UserDetailsResponse() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
