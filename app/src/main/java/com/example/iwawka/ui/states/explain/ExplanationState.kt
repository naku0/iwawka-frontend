package com.example.iwawka.ui.states.explain

data class ExplanationState(
    val explanation: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDialogVisible: Boolean = false,
    val showDialog: Boolean,

    )