package io.github.cmh1448.autojwt.model

class SimpleAuthDetails(
    private val username: String,
    private val name: String,
    private val email: String,
    private val password: String
) : AuthDetails() {
    override fun getKey(): String {
        return username
    }
}