package io.github.cmh1448.autojwt.configuration

import io.github.cmh1448.autojwt.configurer.JwtAutoConfigurer
import io.github.cmh1448.autojwt.configurer.JwtAutoConfigurerFactory
import io.github.cmh1448.autojwt.handler.JwtTokenProvider
import io.github.cmh1448.autojwt.handler.JwtTokenResolver
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.util.StringUtils
import org.springframework.web.servlet.HandlerExceptionResolver
import java.security.Key


@AutoConfiguration
@ConditionalOnClass(JwtAutoConfigurer::class)
@EnableConfigurationProperties(JwtCustomProperties::class)
class AutoJwtAutoConfiguration(
    private val properties: JwtCustomProperties,
    private val handlerExceptionResolver: HandlerExceptionResolver
) {
    companion object {
        private const val MINIMUM_SECRET_LENGTH = 32
        private const val DEFAULT_ACCESS_TOKEN_EXPIRE_HOURS = 1
        private val logger = LoggerFactory.getLogger(AutoJwtAutoConfiguration::class.java)
    }

    val key: Key = initializeKey()
    val accessTokenExpireHours: Number = initializeAccessTokenExpireHours()

    private fun initializeKey(): Key {
        return when {
            !StringUtils.hasText(properties.secret) -> {
                logger.warn("JWT Secret is not set. Using randomly generated secret.")
                Keys.secretKeyFor(SignatureAlgorithm.HS256)
            }
            properties.secret!!.length < MINIMUM_SECRET_LENGTH -> {
                throw IllegalArgumentException("JWT secret must be at least $MINIMUM_SECRET_LENGTH characters long")
            }
            else -> Keys.hmacShaKeyFor(properties.secret!!.toByteArray())
        }
    }

    private fun initializeAccessTokenExpireHours(): Number {
        return properties.accessTokenExpireHours ?: run {
            logger.warn("JWT Access Token Expire Time is not set. Using default value $DEFAULT_ACCESS_TOKEN_EXPIRE_HOURS hour.")
            DEFAULT_ACCESS_TOKEN_EXPIRE_HOURS
        }
    }

    @Bean
    @ConditionalOnMissingBean
    fun jwtTokenProvider(): JwtTokenProvider {
        return JwtTokenProvider(key)
    }

    @Bean
    @ConditionalOnMissingBean
    fun jwtTokenResolver(): JwtTokenResolver {
        return JwtTokenResolver(key)
    }

    @Bean
    @ConditionalOnMissingBean
    fun jwtAutoConfigurerFactory(): JwtAutoConfigurerFactory {
        return JwtAutoConfigurerFactory(
            jwtTokenResolver(),
            handlerExceptionResolver,
            jwtTokenProvider(),
            accessTokenExpireHours
        )
    }

    @Bean
    @ConditionalOnMissingBean
    fun dummyUserDetailsService(): UserDetailsService {
        return UserDetailsService {
            null
        }
    }
}