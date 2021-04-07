package es.com.controller;


import es.com.dto.request.ChangePasswordRequest;
import es.com.dto.response.JwtResponse;
import es.com.dto.request.LoginRequest;
import es.com.model.ERole;
import es.com.model.Role;
import es.com.model.User;
import es.com.model.UserDetails;
import es.com.dto.response.MessageResponse;
import es.com.dto.request.SignupRequest;
import es.com.repository.RoleRepository;
import es.com.repository.UserRepository;
import es.com.security.jwt.JwtUtils;
import es.com.security.services.UserDetailsImpl;
import es.com.service.ImageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @ModelAttribute LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerUser(MultipartFile file,
                                          @Valid @ModelAttribute SignupRequest signUpRequest, BindingResult result) {

        System.out.println(signUpRequest);

        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(err -> log.error(err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Incorrect signup fields"));
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);

        byte[] image = new byte[0];
        try {
            image = ImageService.compressBytes(file.getBytes());
        } catch (IOException e) {
            log.error("Photo compression error");
        }

        UserDetails userDetails = new UserDetails(signUpRequest.getFirstname(),
                signUpRequest.getLastname(), signUpRequest.getDateOfBirth(), signUpRequest.getCity(),
                signUpRequest.getHobbies(), Integer.parseInt(signUpRequest.getPhoneNumber()), image);

        user.setUserDetails(userDetails);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> changePassword(@Valid @ModelAttribute ChangePasswordRequest changePasswordRequest) {
        Optional userOptional = userRepository.findById(Long.valueOf(changePasswordRequest.getUserID()));
        if (userOptional.isPresent()) {
            User user = (User) userOptional.get();
            if (!encoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Bad old password"));
            }
            user.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Password changed successfully!"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("User doesn't exist"));
        }
    }
}
