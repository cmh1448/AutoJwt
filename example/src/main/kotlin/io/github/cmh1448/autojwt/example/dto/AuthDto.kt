package io.github.cmh1448.autojwt.example.dto

import io.github.cmh1448.autojwt.example.model.User
import org.springframework.security.crypto.password.PasswordEncoder

class AuthDto {
    data class Request(
        val id: String,
        val password: String,
    ) {
        fun toUser(
            passwordEncoder: PasswordEncoder
        ): User {
            return User(
                id, passwordEncoder.encode(password)
            )
        }
    }
}