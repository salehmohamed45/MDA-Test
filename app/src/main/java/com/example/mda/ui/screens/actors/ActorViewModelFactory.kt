package com.example.mda.ui.screens.actors

// ViewModel for managing UI state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mda.data.local.dao.ActorDao
import com.example.mda.data.remote.RetrofitInstance
import com.example.mda.data.repository.ActorsRepository

class ActorViewModelFactory(
    private val repository: ActorsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActorViewModel::class.java)) {
            return ActorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        fun createNetworkOnly(): ActorViewModelFactory {
            val repo = ActorsRepository(RetrofitInstance.api, null)
            return ActorViewModelFactory(repo)
        }
    }
}
