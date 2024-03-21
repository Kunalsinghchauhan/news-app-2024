package com.ksc.newsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ksc.newsapp.repository.Repository

class NewsViewModelFactory(val application: Application,val newsRepository: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(application,newsRepository) as T

    }

}