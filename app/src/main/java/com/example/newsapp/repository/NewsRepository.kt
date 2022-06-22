package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.model.Article

class NewsRepository(private val database: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchForNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    fun getSavedNews() = database.articleDao().getAllArticles()

    suspend fun saveArticle(article: Article) = database.articleDao().insertArticle(article)

    suspend fun deleteArticle(article: Article) = database.articleDao().deleteArticle(article)
}