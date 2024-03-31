package autojwt.example.configuration

import autojwt.handler.JwtTokenProvider
import autojwt.handler.JwtTokenResolver
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfiguration(
    @Value("\${jwt.secret}")
    private val secretText: String,
    @Value("\${jwt.expire-days}")
    private val expireDays: Number
) {
    @Bean
    fun jwtTokenProvider() = JwtTokenProvider(secretText)

    @Bean
    fun jwtTokenResolver() = JwtTokenResolver(secretText)
}