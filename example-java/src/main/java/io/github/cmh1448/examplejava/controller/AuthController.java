package io.github.cmh1448.examplejava.controller;

import io.github.cmh1448.examplejava.dto.AuthDto;
import io.github.cmh1448.examplejava.model.User;
import io.github.cmh1448.examplejava.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public void register(@RequestBody AuthDto.Request request) {
        authService.register(request.getId(), request.getPassword());
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthDto.Request request) {
        return authService.login(request.getId(), request.getPassword());
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal User user) {
        return "You are " + user.getId();
    }
}
