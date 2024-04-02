package io.github.cmh1448.autojwt.example.model

import io.github.cmh1448.autojwt.model.AuthDetails

data class User(
    val id: String,
    val password: String
) : AuthDetails() {
    override fun getKey(): String = id

}