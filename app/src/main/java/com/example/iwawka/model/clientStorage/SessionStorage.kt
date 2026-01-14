package com.example.iwawka.model.clientStorage

class SessionStore {
    @Volatile var myUserId: String = "1"

    fun SessionStore.updateCurrentUserId(userId: String) {

    }

}
