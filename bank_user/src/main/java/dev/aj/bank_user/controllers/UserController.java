package dev.aj.bank_user.controllers;

import dev.aj.bank_user.model.dtos.CreateUser;
import dev.aj.bank_user.model.dtos.UserCreated;
import dev.aj.bank_user.services.Auth0UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${bank_user_uri}")
@RequiredArgsConstructor
public class UserController {

    private final Auth0UserService userService;

    @GetMapping("/public")
    public ResponseEntity<String> getPublicEndpoint() {
        return ResponseEntity.ok("You are welcome!!");
    }

    @GetMapping("/secure")
    public ResponseEntity<String> getSecureEndpoint() {
        return ResponseEntity.ok("You are welcome!!");
    }

    @PreAuthorize("hasAuthority('create:users')")
    @PostMapping(path = "/")
    public ResponseEntity<UserCreated> createUser(@RequestBody CreateUser createUserRequest) {
        return ResponseEntity.ok(userService.createNewUser(createUserRequest));
    }

}
