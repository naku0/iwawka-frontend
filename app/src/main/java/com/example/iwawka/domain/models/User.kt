package com.example.iwawka.domain.models

data class User(
    val id: String,
    val name: String,
    val phone: String,
    val status: String, // я бы заменил на енам или че нить другое, чтобы детерминировать поле
)