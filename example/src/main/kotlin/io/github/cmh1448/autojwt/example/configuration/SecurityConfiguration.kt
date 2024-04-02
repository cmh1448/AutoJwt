package io.github.cmh1448.autojwt.example.configuration

import io.github.cmh1448.autojwt.configurer.JwtConfigurerFactory
import io.github.cmh1448.autojwt.service.UserLoadService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val jwtAutoConfigurerFactory: JwtConfigurerFactory,
    private val userLoadService: UserLoadService
)  {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val jwtAutoConfigurer = jwtAutoConfigurerFactory.createJwtAutoConfigurer(userLoadService)

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