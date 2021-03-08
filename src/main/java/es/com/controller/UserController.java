package es.com.controller;

import es.com.dto.request.ChangePasswordRequest;
import es.com.dto.request.UpdateDataRequest;
import es.com.dto.response.AllUserListUserDetails;
import es.com.dto.response.MessageResponse;
import es.com.dto.response.UserDetailsResponse;
import es.com.dto.request.UserPostRequest;
import es.com.dto.response.UserPostResponse;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
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
            UserPost userPost = new UserPost(userPostRequest.getUserID(), userPostRequest.getTitle(), userPostRequest.getContent(), userPostRequest.getDate());
            userPostRepository.save(userPost);
        }

        return ResponseEntity.ok(new MessageResponse("post message"));
    }

    @GetMapping("/userPosts/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserPosts(@PathVariable String id) {
        List<UserPost> userPosts = userPostRepository.findByUserID(id);

        List<UserPostResponse> result = new ArrayList<>();
        for(UserPost userPost: userPosts) {
            UserPostResponse response = new UserPostResponse(userPost.getTitle(), userPost.getContent(), userPost.getDate());
            result.add(response);
        }
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUserData(MultipartFile file, @Valid @ModelAttribute UpdateDataRequest updateDataRequest, BindingResult result) {

        if(result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(err -> log.error(err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Incorrect update fields"));
        }

        Optional<User> userOptional = userRepository.findById(Long.valueOf(updateDataRequest.getId()));
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.getUserDetails().setFirstname(updateDataRequest.getFirstname());
            user.getUserDetails().setLastname(updateDataRequest.getLastname());
            user.getUserDetails().setHobbies(updateDataRequest.getHobbies());
            user.getUserDetails().setPhoneNumber(Integer.parseInt(updateDataRequest.getPhoneNumber()));

            byte[] image = new byte[0];
            try {
                image = ImageService.compressBytes(file.getBytes());
            } catch (IOException e) {
                log.error("Photo compression error");
            }
            user.getUserDetails().setImageBytes(image);

            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("User data updated successfully!"));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("User doesn't exist"));
    }
}
