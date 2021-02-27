package es.com.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "user_post")
@Getter
@Setter
@ToString
public class UserPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_user_id")
    private Long id;
    @Column(nullable = false)
    private String userID;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column
    private String date;

    public UserPost(String userID, String title, String content, String date) {
        this.userID = userID;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public UserPost() {
    }
}
