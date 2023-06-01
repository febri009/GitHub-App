package com.example.github_app.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun <T: ViewModel> T.createFactory(): ViewModelProvider.Factory {
    val viewModel = this
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return viewModel as T
        }
    }
}