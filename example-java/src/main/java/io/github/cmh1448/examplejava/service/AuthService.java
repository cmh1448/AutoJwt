package io.github.cmh1448.examplejava.service;

import io.github.cmh1448.autojwt.handler.JwtTokenProvider;
import io.github.cmh1448.examplejava.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public String login(String id, String password) {
        return userService.findById(id)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> jwtTokenProvider.generate(user, 24))
                .orElseThrow(() -> new RuntimeException("email or password is wrong"));
    }

    public void register(String id, String password) {
        User toSave = User.builder()
                .id(id)
                .password(passwordEncoder.encode(password))
                .build();

        userService.addUser(toSave);
    }
}
