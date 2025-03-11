package io.github.cmh1448.autojwt.model

import java.time.LocalDateTime

class JwtToken (
    val type: String,
    val expireAt: LocalDateTime,
    val subject: String
) {
    fun isAccessToken(): Boolean {
        return type == "access"
    }

    fun isRefreshToken(): Boolean {
        return type == "refresh"
    }
}