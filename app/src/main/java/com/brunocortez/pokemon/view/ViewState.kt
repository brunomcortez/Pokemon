package com.brunocortez.pokemon.view

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Failed(val throwable: Throwable) : ViewState<Nothing>()
}