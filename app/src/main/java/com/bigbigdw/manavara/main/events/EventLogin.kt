package com.bigbigdw.manavara.main.events

sealed interface EventLogin{
    object Loaded: EventLogin
}

data class StateLogin(
    val Loaded: Boolean = false,
)