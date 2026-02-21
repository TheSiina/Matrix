package com.example.matrix

// JobViewModel.kt
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class JobViewModel(private val repository: JobRepository) : ViewModel() {

    // وضعیت (State) لیست مشاغل
    var jobs by mutableStateOf<List<Job>>(emptyList())
        private set

    // وضعیت بارگذاری (برای نشون دادن Loading)
    var isLoading by mutableStateOf(true)
        private set

    init {
        loadJobs()
    }

    private fun loadJobs() {
        viewModelScope.launch {
            isLoading = true
            jobs = repository.getAllJobs()
            isLoading = false
        }
    }
}