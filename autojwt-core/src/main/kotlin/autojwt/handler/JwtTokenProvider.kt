package autojwt.handler

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class JwtTokenProvider(
    private val secret: String
) {
    private val secretKey: Key = Keys.hmacShaKeyFor(secret.toByteArray())
    fun generate(
        name: String,
        expireHours: Long
    ): String {
        val claims = Jwts.claims().setSubject(name)

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