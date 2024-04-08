package io.github.cmh1448.examplejava.dto;

import io.github.cmh1448.examplejava.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Request {
        private String id;
        private String password;

        public User toUser(PasswordEncoder encoder) {
            return User.builder()
                    .id(id)
                    .password(encoder.encode(password))
                    .build();
        }
    }
}
