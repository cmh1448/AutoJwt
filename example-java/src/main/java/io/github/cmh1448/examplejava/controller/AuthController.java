package io.github.cmh1448.examplejava.controller;

import io.github.cmh1448.examplejava.model.User;
import io.github.cmh1448.examplejava.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public void register(String id, String password) {
        authService.register(id, password);
    }

    @PostMapping("/login")
    public String login(String id, String password) {
        return authService.login(id, password);
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal User user) {
        return "You are " + user.getId();
    }
}
