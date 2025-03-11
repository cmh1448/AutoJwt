package io.github.cmh1448.autojwt.filter

import io.github.cmh1448.autojwt.handler.JwtTokenProvider
import io.github.cmh1448.autojwt.model.JwtToken
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import java.time.format.DateTimeFormatter

class JwtRefreshTokenFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val accessTokenExpireHours: Number
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token: JwtToken? = request.getAttribute("JwtToken") as JwtToken?
        if(token != null && token.isRefreshToken()) {
            val refreshed = jwtTokenProvider.refreshAccessToken(token, accessTokenExpireHours)

            response.setHeader("Refreshed-Access-Token", refreshed.tokenString)
            response.setHeader("Refreshed-Access-Token-Expire", refreshed.expireAt.format(DateTimeFormatter.ISO_DATE_TIME))

            filterChain.doFilter(request, response)
        } else {
            filterChain.doFilter(request, response)
        }
    }
}