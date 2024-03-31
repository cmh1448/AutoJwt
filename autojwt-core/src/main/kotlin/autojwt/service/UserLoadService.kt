package autojwt.service

import autojwt.model.AuthDetails

interface UserLoadService {
    fun loadUserByKey(key: String): AuthDetails
}