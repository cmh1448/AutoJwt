package autojwt.configuration

import autojwt.configurer.JwtAutoConfigurer
import autojwt.configurer.JwtConfigurerFactory
import autojwt.handler.JwtTokenProvider
import autojwt.handler.JwtTokenResolver
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.HandlerExceptionResolver


@AutoConfiguration
@ConditionalOnClass(JwtAutoConfigurer::class)
class AutoJwtAutoConfiguration(
    @Value("\${autojwt.secret}")
    val  secret: String,
    val handlerExceptionResolver: HandlerExceptionResolver
) {
    private val logger = LoggerFactory.getLogger(AutoJwtAutoConfiguration::class.java)

    init {
        logger.info("AutoJwt Configuration Initialized")
    }

    @Bean
    @ConditionalOnMissingBean
    fun jwtTokenProvider() = JwtTokenProvider(secret)
    @Bean
    @ConditionalOnMissingBean
    fun jwtTokenResolver() = JwtTokenResolver(secret)
    @Bean
    @ConditionalOnMissingBean
    fun jwtConfigurerFactory() = JwtConfigurerFactory(jwtTokenResolver(), handlerExceptionResolver)
}