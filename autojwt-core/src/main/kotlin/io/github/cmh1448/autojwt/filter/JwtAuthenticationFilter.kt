package io.github.cmh1448.autojwt.filter

import io.github.cmh1448.autojwt.exception.JwtException
import io.github.cmh1448.autojwt.exception.JwtInvalidException
import io.github.cmh1448.autojwt.exception.JwtMissingException
import io.github.cmh1448.autojwt.handler.JwtTokenResolver
import io.github.cmh1448.autojwt.model.JwtToken
import io.github.cmh1448.autojwt.service.UserLoadService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtAuthenticationFilter(
    private val userLoadService: UserLoadService,
    private val handlerExceptionResolver: HandlerExceptionResolver
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val token = request.getAttribute("JwtToken") as JwtToken?

        if (token != null) {
            try {
                val userDetails = userLoadService.loadUserByKey(token.subject)
                if (userDetails.isEmpty) {
                    throw JwtInvalidException()
                }

                SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                    userDetails.get(), null, mutableListOf(SimpleGrantedAuthority("USER"))
                )
                filterChain.doFilter(request, response)
            } catch (e: Exception) {
                if (e is JwtException) {
                    handlerExceptionResolver.resolveException(request, response, null, e)
                } else {
                    handlerExceptionResolver.resolveException(
                        request, response, null, JwtException("Authentication Failed", e)
                    )
                }
            }
        } else {
            filterChain.doFilter(request, response)
        }
    }
}