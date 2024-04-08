package io.github.cmh1448.examplejava.exception;

import io.github.cmh1448.autojwt.exception.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtException.class)
    public String handleJwtException(JwtException e) {
        log.error("인증 과정에서 오류가 발생했습니다.", e);
        return Optional.ofNullable(e.getMessage())
                .orElse("인증 과정에서 오류가 발생했습니다.");
    }
}
