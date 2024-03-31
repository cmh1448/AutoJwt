package autojwt.configurer

import autojwt.handler.JwtTokenResolver
import autojwt.service.UserLoadService
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtConfigurerFactory(
    private val jwtTokenResolver: JwtTokenResolver,
    private val handlerExceptionResolver: HandlerExceptionResolver
) {
    fun createJwtAutoConfigurer(userLoadService: UserLoadService): JwtAutoConfigurer {
        return JwtAutoConfigurer(jwtTokenResolver, userLoadService, handlerExceptionResolver)
    }
}