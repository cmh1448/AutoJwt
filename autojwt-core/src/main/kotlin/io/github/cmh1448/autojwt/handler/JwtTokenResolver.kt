package io.github.cmh1448.autojwt.handler

import io.github.cmh1448.autojwt.exception.JwtExpiredException
import io.github.cmh1448.autojwt.exception.JwtInvalidException
import io.github.cmh1448.autojwt.exception.JwtParseException
import io.github.cmh1448.autojwt.model.JwtToken
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import java.security.Key
import java.time.ZoneId

class JwtTokenResolver(
    private val secretKey: Key
) {

    fun parseTokenFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization") ?: return ""

        return if (bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    fun resolveKeyFromToken(token: String): JwtToken {
        try {
            val parsed = Jwts.parserBuilder()
                .setSigningKey(secretKey).build()
                .parseClaimsJws(token);

            return JwtToken(
                type = parsed.body.get("type", String::class.java),
                expireAt = parsed.body.expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                subject = parsed.body.subject
            )
        } catch (e: ExpiredJwtException) {
            throw JwtExpiredException(e)
        } catch (e: SignatureException) {
            throw JwtInvalidException(e)
        } catch (e: Exception) {
            throw JwtParseException(e)
        }
    }
}