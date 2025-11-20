package com.example.iwawka.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.iwawka.ui.states.UiState
import com.example.iwawka.ui.states.MessageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {
    private val _state = MutableStateFlow(UiState())
    private val _message = MessageState()
    val state: StateFlow<UiState> = _state.asStateFlow()



}
