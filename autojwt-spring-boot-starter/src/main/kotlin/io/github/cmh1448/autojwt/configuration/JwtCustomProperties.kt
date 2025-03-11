package io.github.cmh1448.autojwt.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
class JwtCustomProperties (
    val secret: String?,
    val accessTokenExpireHours: Number?
)