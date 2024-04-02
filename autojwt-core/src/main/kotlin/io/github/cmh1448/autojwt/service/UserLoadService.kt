package io.github.cmh1448.autojwt.service

import io.github.cmh1448.autojwt.model.AuthDetails

interface UserLoadService {
    fun loadUserByKey(key: String): AuthDetails
}