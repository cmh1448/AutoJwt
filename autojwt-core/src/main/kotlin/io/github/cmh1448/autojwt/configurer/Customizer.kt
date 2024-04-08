package io.github.cmh1448.autojwt.configurer

fun interface Customizer<T> {
    fun customize(t: T)
}