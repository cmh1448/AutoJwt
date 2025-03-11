package io.github.cmh1448.autojwt.handler

import io.github.cmh1448.autojwt.model.AuthDetails
import io.github.cmh1448.autojwt.model.JwtDto
import io.github.cmh1448.autojwt.model.JwtToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class JwtTokenProvider(
    private val secret: Key,
    private val clock: () -> LocalDateTime = { LocalDateTime.now() } // 테스트 용이성을 위한 의존성 주입
) {
    companion object {
        private val ZONE_ID = ZoneId.systemDefault()
    }

    private fun createToken(claims: Claims, expireHours: Number, type: String): JwtDto.TokenData {
        val expireLocalDateTime = clock().plusSeconds(expireHours.toLong())
        val expireDate = Date.from(expireLocalDateTime.atZone(ZONE_ID).toInstant())

        val tokenString = Jwts.builder()
            .setClaims(claims)
            .setExpiration(expireDate)
            .setIssuedAt(expireDate)
            .signWith(secret)
            .claim("type", type)
            .compact()

        return JwtDto.TokenData(tokenString, expireLocalDateTime)
    }

    fun generateAccessToken(user: AuthDetails, expireHours: Number): JwtDto.TokenData =
        createToken(Jwts.claims().setSubject(user.getKey()), expireHours, "access")

    fun refreshAccessToken(expiredAccessToken: JwtToken, expireHours: Number): JwtDto.TokenData =
        createToken(Jwts.claims().setSubject(expiredAccessToken.subject), expireHours, "access")

    fun generateRefreshToken(user: AuthDetails, expireHours: Number): JwtDto.TokenData =
        createToken(Jwts.claims().setSubject(user.getKey()), expireHours, "refresh")
}