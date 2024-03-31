package autojwt.example.dto

import autojwt.example.model.User
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