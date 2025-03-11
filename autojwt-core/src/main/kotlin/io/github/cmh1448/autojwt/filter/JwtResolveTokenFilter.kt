package io.github.cmh1448.autojwt.filter

import io.github.cmh1448.autojwt.exception.JwtException
import io.github.cmh1448.autojwt.exception.JwtMissingException
import io.github.cmh1448.autojwt.handler.JwtTokenResolver
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtResolveTokenFilter(
    private val jwtTokenResolver: JwtTokenResolver,
    private val handlerExceptionResolver: HandlerExceptionResolver,
    private val ignorePatterns: List<String>,
    private val allowedPatterns: List<String>,
) : OncePerRequestFilter() {
    private val antPathMatcher = AntPathMatcher()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (this.isMatchingURI(request.requestURI)) {
            try {
                val token = jwtTokenResolver.parseTokenFromRequest(request) ?: throw JwtMissingException()
                val jwtToken = jwtTokenResolver.resolveKeyFromToken(token)
                request.setAttribute("JwtToken", jwtToken)
                filterChain.doFilter(request, response)
            } catch (e: Exception) {
                if (e is JwtException) {
                    handlerExceptionResolver.resolveException(request, response, null, e)
                } else {
                    handlerExceptionResolver.resolveException(
                        request,
                        response,
                        null,
                        JwtException("Authentication Failed", e)
                    )
                }
            }
        } else {
            filterChain.doFilter(request, response)
        }
    }

    private fun isMatchingURI(servletPath: String): Boolean {
        if (allowedPatterns.any { antPathMatcher.match(it, servletPath) }) {
            return !ignorePatterns.any { antPathMatcher.match(it, servletPath) }
        }

        return false
    }
}