package dev.aj.bank_user.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${bank_user_uri}")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/public")
    public ResponseEntity<String> getPublicEndpoint() {
        return ResponseEntity.ok("You are welcome!!");
    }

}
