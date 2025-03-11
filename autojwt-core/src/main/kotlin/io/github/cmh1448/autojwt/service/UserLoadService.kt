package io.github.cmh1448.autojwt.service

import io.github.cmh1448.autojwt.model.AuthDetails
import java.util.Optional

interface UserLoadService {
    fun loadUserByKey(key: String): Optional<out AuthDetails>
}