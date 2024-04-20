package com.ksc.newsapp.ui


import android.content.Context
import android.graphics.drawable.Icon
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.ksc.newsapp.databinding.ActivityNewsBinding
import com.ksc.newsapp.models.Article
import com.ksc.newsapp.models.NewsResponse

import com.ksc.newsapp.repository.NewsRepository
import com.ksc.newsapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var breakingNewsPage = 1
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var searchNewsPage = 1

    init {
        getBreakingNews("in")
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        try {
            val response = newsRepository.getHeadlines(countryCode, breakingNewsPage)
            breakingNews.postValue(handleBreakingNewsResponse(response))
        } catch (e: Exception) {
            breakingNews.postValue(Resource.Error(""))
        }
    }


    fun getSearchNews(searchQuery: String, context: Context) = viewModelScope.launch {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting

        if (!isConnected) {
            Toast.makeText(context, "Not Connected to Internet", Toast.LENGTH_SHORT).apply {

                show()
            }
            return@launch
        }
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.getSearchEverything(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun favouriteArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getFavouriteNews() = newsRepository.getFavouriteNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }



}