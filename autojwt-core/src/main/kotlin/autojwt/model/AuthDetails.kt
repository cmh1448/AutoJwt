package autojwt.model

import org.springframework.security.core.AuthenticatedPrincipal
abstract class AuthDetails : AuthenticatedPrincipal {
    abstract fun getKey(): String

    override fun getName(): String {
        return getKey()
    }
}