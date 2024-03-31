package autojwt.exception

import org.springframework.security.core.AuthenticationException

open class JwtException: AuthenticationException {
    constructor(msg: String, t: Throwable): super(msg, t)
    constructor(msg: String): super(msg)
    constructor(): super("인증 과정에서 오류가 발생했습니다.")
}

// Variation of JwtException
class JwtExpiredException() : JwtException("토큰이 만료되었습니다.") {
    constructor(t: Throwable) : this() {
        initCause(t)
    }
}
class JwtInvalidException() : JwtException("토큰이 유효하지 않습니다.") {
    constructor(t: Throwable) : this() {
        initCause(t)
    }
}
class JwtMissingException() : JwtException("토큰이 없습니다.") {
    constructor(t: Throwable) : this() {
        initCause(t)
    }
}
class JwtParseException() : JwtException("토큰을 파싱하는 중 오류가 발생했습니다.") {
    constructor(t: Throwable) : this() {
        initCause(t)
    }
}


