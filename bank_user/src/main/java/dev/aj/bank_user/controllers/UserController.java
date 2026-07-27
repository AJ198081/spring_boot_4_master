package dev.aj.bank_user.controllers;

import dev.aj.bank_user.model.dtos.CreateUser;
import dev.aj.bank_user.model.dtos.UserCreated;
import dev.aj.bank_user.services.Auth0UserService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final RedisTemplate<String, Object> redisTemplate;


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

    @DeleteMapping("/cache")
    public ResponseEntity<HttpStatus> clearCache(@PathParam("cache_key") String key) {

        return redisTemplate.delete(key)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();

    }

}
