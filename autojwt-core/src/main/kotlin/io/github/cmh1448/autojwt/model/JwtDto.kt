package io.github.cmh1448.autojwt.model

import java.time.LocalDateTime

class JwtDto {
    class TokenData (
        val tokenString: String,
        val expireAt: LocalDateTime
    )
}