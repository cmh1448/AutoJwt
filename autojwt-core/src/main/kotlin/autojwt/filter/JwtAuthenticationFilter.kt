package autojwt.filter

import autojwt.exception.JwtException
import autojwt.exception.JwtMissingException
import autojwt.handler.JwtTokenResolver
import autojwt.service.UserLoadService
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
    private val jwtTokenResolver: JwtTokenResolver,
    private val handlerExceptionResolver: HandlerExceptionResolver,
    private val userLoadService: UserLoadService,
    private val ignorePatterns: List<String>,
    private val includePattern: List<String>
): OncePerRequestFilter() {
    private val pathMatcher: AntPathMatcher = AntPathMatcher()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if(!isMatchingURI(request.servletPath)) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val token = jwtTokenResolver.parseTokenFromRequest(request) ?: throw JwtMissingException()

            val key = jwtTokenResolver.resolveKeyFromToken(token)
            val user = userLoadService.loadUserByKey(key)

            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(user, null, listOf(SimpleGrantedAuthority("User")))
            filterChain.doFilter(request, response)
        } catch (e: JwtException) {
            handlerExceptionResolver.resolveException(request, response, null, e)
        }
    }

    private fun isMatchingURI(servletPath: String): Boolean {
        return if(includePattern.stream().anyMatch { pathMatcher.match(it, servletPath) }) {
            ignorePatterns.stream().noneMatch { pathMatcher.match(it, servletPath) }
        }else
            false
    }
}