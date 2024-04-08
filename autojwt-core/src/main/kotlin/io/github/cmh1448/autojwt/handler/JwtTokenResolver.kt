package io.github.cmh1448.autojwt.handler

import io.github.cmh1448.autojwt.exception.JwtParseException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import java.security.Key

class JwtTokenResolver(
    secret: String
) {
    private val secretKey: Key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun parseTokenFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization") ?: return ""

        return if (bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    fun resolveKeyFromToken(token: String): String {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).body.subject
        } catch (e: Exception) {
            throw JwtParseException(e)
        }

    }
}