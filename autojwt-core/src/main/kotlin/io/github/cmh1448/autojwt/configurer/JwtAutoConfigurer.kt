package io.github.cmh1448.autojwt.configurer


import io.github.cmh1448.autojwt.filter.JwtAuthenticationFilter
import io.github.cmh1448.autojwt.handler.JwtTokenResolver
import io.github.cmh1448.autojwt.service.UserLoadService
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtAutoConfigurer(
    private val jwtTokenResolver: JwtTokenResolver,
    private val userLoadService: UserLoadService,
    private val handlerExceptionResolver: HandlerExceptionResolver
){
    private val pathPatternConfigurer = PathPatternConfigurer()

    fun pathConfigure(customizer: Customizer<PathPatternConfigurer>): JwtAutoConfigurer {
        customizer.customize(pathPatternConfigurer)
        return this
    }

    fun configure(http: HttpSecurity) {
        val jwtAuthenticationFilter = JwtAuthenticationFilter(
            jwtTokenResolver,
            handlerExceptionResolver,
            userLoadService,
            pathPatternConfigurer.excludePatternList,
            pathPatternConfigurer.includePatternList
        )

        http.headers {
            it.frameOptions { option -> option.sameOrigin() }
            }
            .httpBasic {
                it.disable()
            }
            .csrf {
                it.disable()
            }
            .formLogin {
                it.disable()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}