package es.com.controller;

import es.com.dto.ExtendedUserResponse;
import es.com.dto.MessageResponse;
import es.com.dto.UserDetailsResponse;
import es.com.model.User;
import es.com.model.UserDetails;
import es.com.repository.UserRepository;
import es.com.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserDetails(@PathVariable String id) {

        Optional<User> userOptional = userRepository.findById(Long.parseLong(id));
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            byte[] pictureBytes = ImageService.decompressBytes(user.getUserDetails().getImageBytes());
            return ResponseEntity.ok(new UserDetailsResponse(user.getUserDetails().getFirstname(),
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
            ArrayList<ExtendedUserResponse> responseList = new ArrayList<>();
            for(User user: allUsers) {
                UserDetails details = user.getUserDetails();
                ExtendedUserResponse resultUser = new ExtendedUserResponse(user.getUsername(),
                        details.getFirstname(), details.getLastname(), details.getHobbies(),
                        String.valueOf(details.getPhoneNumber()), user.getEmail());
                responseList.add(resultUser);
            }
            return ResponseEntity.ok(responseList);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error while obtaining users list"));
    }
}
