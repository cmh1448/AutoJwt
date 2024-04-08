package io.github.cmh1448.autojwt.handler

import io.github.cmh1448.autojwt.model.AuthDetails
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class JwtTokenProvider(
    secret: String
) {
    private val secretKey: Key = Keys.hmacShaKeyFor(secret.toByteArray())
    fun generate(
        user: AuthDetails,
        expireHours: Long
    ): String {
        val claims = Jwts.claims().setSubject(user.getKey())

        val expiresAt = Date.from(
            LocalDateTime.now().plusHours(expireHours).atZone(ZoneId.systemDefault()).toInstant()
        )

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(expiresAt)
            .signWith(secretKey)
            .compact()
    }
}