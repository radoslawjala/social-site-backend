package es.com.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "user_details")
@Getter
@Setter
@ToString
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_details_id")
    private Long id;
    private String firstname;
    private String lastname;
    private String dateOfBirth;
    private String city;
    private String hobbies;
    @Column(name = "phone_number")
    private int phoneNumber;
    @Column(name = "image_bytes", length = 1000)
    @ToString.Exclude
    private byte[] imageBytes;

    public UserDetails() {
    }

    public UserDetails(String firstname, String lastname, String dateOfBirth,
                       String city, String hobbies, int phoneNumber, byte[] imageBytes) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.hobbies = hobbies;
        this.phoneNumber = phoneNumber;
        this.imageBytes = imageBytes;
    }
}
