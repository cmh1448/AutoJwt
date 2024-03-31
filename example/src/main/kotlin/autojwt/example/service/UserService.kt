package autojwt.example.service

import autojwt.example.dto.AuthDto
import autojwt.example.model.User
import autojwt.model.AuthDetails
import autojwt.service.UserLoadService
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