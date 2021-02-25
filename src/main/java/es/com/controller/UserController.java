package es.com.controller;

import es.com.dto.AllUserListUserDetails;
import es.com.dto.MessageResponse;
import es.com.dto.UserDetailsResponse;
import es.com.dto.UserPostRequest;
import es.com.model.User;
import es.com.model.UserDetails;
import es.com.model.UserPost;
import es.com.repository.UserPostRepository;
import es.com.repository.UserRepository;
import es.com.service.ImageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Log4j2
@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPostRepository userPostRepository;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserDetails(@PathVariable String id) {

        Optional<User> userOptional = userRepository.findById(Long.parseLong(id));
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            byte[] pictureBytes = ImageService.decompressBytes(user.getUserDetails().getImageBytes());
            return ResponseEntity.ok(new UserDetailsResponse(user.getUsername(), user.getUserDetails().getFirstname(),
                    user.getUserDetails().getLastname(), user.getUserDetails().getHobbies(),
                    String.valueOf(user.getUserDetails().getPhoneNumber()), pictureBytes));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: Something bad happened"));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        if(allUsers != null) {
            ArrayList<AllUserListUserDetails> responseList = new ArrayList<>();
            for(User user: allUsers) {
                UserDetails details = user.getUserDetails();
                byte[] pictureBytes = ImageService.decompressBytes(details.getImageBytes());
                AllUserListUserDetails resultUser = new AllUserListUserDetails(String.valueOf(user.getId()),
                        user.getUsername(), details.getFirstname(), details.getLastname(), pictureBytes);
                responseList.add(resultUser);
            }
            return ResponseEntity.ok(responseList);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error while obtaining users list"));
    }

    @PostMapping("/add-post")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> addUserPost(@Valid @ModelAttribute UserPostRequest userPostRequest, BindingResult result) {

        if(result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(err -> log.error(err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Incorrect post fields"));
        }
        else {
            UserPost userPost = new UserPost(userPostRequest.getUserID(), userPostRequest.getTitle(), userPostRequest.getContent());
            userPostRepository.save(userPost);
        }

        return ResponseEntity.ok(new MessageResponse("post message"));
    }
}
