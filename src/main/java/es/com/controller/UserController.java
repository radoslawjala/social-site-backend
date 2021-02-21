package es.com.controller;

import es.com.dto.MessageResponse;
import es.com.dto.UserDetailsResponse;
import es.com.model.User;
import es.com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.ok(new UserDetailsResponse(user.getUserDetails().getFirstname(),
                    user.getUserDetails().getLastname(), user.getUserDetails().getHobbies(),
                    String.valueOf(user.getUserDetails().getPhoneNumber())));
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Error: Something bad happened"));
    }
}
