package autojwt.example.configuration

import autojwt.configurer.JwtAutoConfigurer
import autojwt.handler.JwtTokenResolver
import autojwt.service.UserLoadService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val jwtTokenResolver: JwtTokenResolver,
    private val userLoadService: UserLoadService,
    private val handlerExceptionResolver: HandlerExceptionResolver
)  {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val jwtAutoConfigurer = JwtAutoConfigurer(jwtTokenResolver, userLoadService, handlerExceptionResolver)

        jwtAutoConfigurer
            .pathConfigure {
                it.includeAll()
                it.excludePattern("/register")
                it.excludePattern("/login")
            }
            .configure(http)

        return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}