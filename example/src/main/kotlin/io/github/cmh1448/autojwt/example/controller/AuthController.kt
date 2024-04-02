package io.github.cmh1448.autojwt.example.controller

import io.github.cmh1448.autojwt.example.dto.AuthDto
import io.github.cmh1448.autojwt.example.service.AuthService
import io.github.cmh1448.autojwt.model.AuthDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(
        @RequestBody req: AuthDto.Request
    ) {
        authService.register(req)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody req: AuthDto.Request
    ): String {
        return authService.login(req)
    }


    @GetMapping("/test")
    fun test(
        @AuthenticationPrincipal userDetails: AuthDetails
    ): String {
        return "You are logged in as ${userDetails.name}"
    }
}