package io.github.cmh1448.autojwt.example.service

import io.github.cmh1448.autojwt.example.dto.AuthDto
import io.github.cmh1448.autojwt.example.model.User
import io.github.cmh1448.autojwt.model.AuthDetails
import io.github.cmh1448.autojwt.service.UserLoadService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService: UserLoadService {
    private val cachedUsers = mutableListOf<User>()

    fun addUser(user: User) {
        cachedUsers.add(user)
    }

    fun findUserById(id: String): User? {
        return cachedUsers.find { it.id == id }
    }

    override fun loadUserByKey(key: String): AuthDetails {
        return cachedUsers.find { it.id == key } ?: throw IllegalArgumentException("User not found")
    }
}