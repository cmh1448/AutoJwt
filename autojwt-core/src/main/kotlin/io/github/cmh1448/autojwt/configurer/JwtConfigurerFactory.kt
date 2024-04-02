package io.github.cmh1448.autojwt.configurer

import io.github.cmh1448.autojwt.handler.JwtTokenResolver
import io.github.cmh1448.autojwt.service.UserLoadService
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtConfigurerFactory(
    private val jwtTokenResolver: JwtTokenResolver,
    private val handlerExceptionResolver: HandlerExceptionResolver
) {
    fun createJwtAutoConfigurer(userLoadService: UserLoadService): JwtAutoConfigurer {
        return JwtAutoConfigurer(jwtTokenResolver, userLoadService, handlerExceptionResolver)
    }
}