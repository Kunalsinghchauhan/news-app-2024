package com.ksc.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ksc.newsapp.models.Article
import com.ksc.newsapp.models.NewsResponse
import com.ksc.newsapp.repository.Repository
import com.ksc.newsapp.utils.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(app: Application, private val newsRepository: Repository) :
    AndroidViewModel(app) {
    val headlines: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var headlinePage = 1
    private var headlinesResponse: NewsResponse? = null
    var searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null
    private var newSearchQuery: String? = null
    private var oldSearchQuery: String? = null
    fun getHeadlines(countryCode: String) = viewModelScope.launch {
        headlinesInternet(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsInternet(searchQuery)
    }
    init {
        getHeadlines("in")
    }

    fun handleHeadlinesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headlinePage++
                if (headlinesResponse == null) {
                    headlinesResponse = resultResponse
                } else {
                    val oldArticle = headlinesResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun handleSearchResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun addToFavourite(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getFavourite() = newsRepository.getFavouriteNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    private suspend fun headlinesInternet(countryCode: String) {
        headlines.postValue(Resource.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = newsRepository.getHeadlines(countryCode, headlinePage)
                headlines.postValue(handleHeadlinesResponse(response))
            } else {
                headlines.postValue(Resource.Error("No Internet"))
            }
        } catch (err: Throwable) {
            when (err) {
                is IOException -> headlines.postValue(Resource.Error("Unable to Connect"))
                else -> headlines.postValue(Resource.Error("No Signal"))
            }
        }
    }

    private suspend fun searchNewsInternet(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = newsRepository.getSearchEverything(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No Internet"))
            }
        } catch (err: Throwable) {
            when (err) {
                is IOException -> searchNews.postValue(Resource.Error("Unable to Connect"))
                else -> searchNews.postValue(Resource.Error("No Signal"))
            }
        }
    }


}