package autojwt.example.model

import autojwt.model.AuthDetails

data class User(
    val id: String,
    val password: String
) : AuthDetails() {
    override fun getKey(): String = id

}