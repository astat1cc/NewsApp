package com.example.newsapp.ui

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.repository.NewsRepository

class NewsViewModelFactory(val repository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            NewsViewModel::class.java -> NewsViewModel(repository) as T
            else -> throw Error("Unknown view model class")
        }
    }
}

fun Activity.factory(repository: NewsRepository) = NewsViewModelFactory(repository)