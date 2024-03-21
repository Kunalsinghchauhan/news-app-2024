package com.ksc.newsapp.repository

import com.ksc.newsapp.api.RetrofitInstance
import com.ksc.newsapp.db.ArticleDatabase
import com.ksc.newsapp.models.Article

class Repository(private val db: ArticleDatabase) {
    suspend fun getHeadlines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getHeadlines(countryCode, pageNumber)

    suspend fun getSearchEverything(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.getSearchEverything(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDAO().upsert(article)
    fun getFavouriteNews() = db.getArticleDAO().getAllArticle()
    suspend fun deleteArticle(article: Article) = db.getArticleDAO().deleteArticle(article)
}



