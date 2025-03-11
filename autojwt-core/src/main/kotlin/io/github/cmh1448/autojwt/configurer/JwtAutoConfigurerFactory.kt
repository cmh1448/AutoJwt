package io.github.cmh1448.autojwt.configurer

import io.github.cmh1448.autojwt.handler.JwtTokenProvider
import io.github.cmh1448.autojwt.handler.JwtTokenResolver
import io.github.cmh1448.autojwt.service.UserLoadService
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtAutoConfigurerFactory(
    private val jwtTokenResolver: JwtTokenResolver,
    private val handlerExceptionResolver: HandlerExceptionResolver,
    private val jwtTokenProvider: JwtTokenProvider,
    private val accessTokenExpireHours: Number
) {
    fun createJwtAutoConfigurer(userLoadService: UserLoadService): JwtAutoConfigurer {
        return JwtAutoConfigurer(
            jwtTokenResolver,
            jwtTokenProvider,
            userLoadService,
            handlerExceptionResolver,
            accessTokenExpireHours
        )
    }
}