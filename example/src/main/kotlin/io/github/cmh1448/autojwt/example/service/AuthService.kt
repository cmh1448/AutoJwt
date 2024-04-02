package io.github.cmh1448.autojwt.example.service

import io.github.cmh1448.autojwt.example.dto.AuthDto
import io.github.cmh1448.autojwt.handler.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) {
    fun register(request: AuthDto.Request) {
        userService.addUser(request.toUser(passwordEncoder))
    }

    fun login(request: AuthDto.Request): String {
        val toLogin = userService.findUserById(request.id)

        if (toLogin == null || !passwordEncoder.matches(request.password, toLogin.password)) {
            throw IllegalArgumentException("Invalid user or password")
        }

        return jwtTokenProvider.generate(
            toLogin.id,
            24
        )
    }
}