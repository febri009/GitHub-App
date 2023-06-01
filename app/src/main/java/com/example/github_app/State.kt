package com.example.github_app

class State<out T>(private val content: T) {

    companion object {
        const val DATA_NULL = "ERROR : DATA TIDAK ADA ATAU RESPON TIDAK BERHASIL"
        const val NO_RESPON = "ERROR : GAGAL MENERIMA DATA DARI API"
    }

    @Suppress("memberVisibilityCanBePrivate")
    var handled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (handled) {
            null
        } else {
            handled = true
            content
        }
    }
}