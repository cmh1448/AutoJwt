package io.github.cmh1448.autojwt.example.exception

import io.github.cmh1448.autojwt.exception.JwtException
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(JwtException::class)
    fun handleJwtException(e: JwtException): String {
        logger.error("JwtException: ${e.message}")
        return e.message ?: "인증 과정에서 오류가 발생했습니다."
    }
}