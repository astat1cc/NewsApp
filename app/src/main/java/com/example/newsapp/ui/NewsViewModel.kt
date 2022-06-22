package com.example.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.Article
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Result
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _breakingNews: MutableLiveData<Result<NewsResponse>> = MutableLiveData()
    val breakingNews = _breakingNews

    private val _searchedNews: MutableLiveData<Result<NewsResponse>> = MutableLiveData()
    val searchedNews = _searchedNews

    private val breakingNewsResponse: NewsResponse? = null
    private var breakingNewsPage = 1

    private val searchForNewsResponse: NewsResponse? = null
    private var searchedNewsPage = 1

    fun searchForNews(searchQuery: String) = viewModelScope.launch {
        _searchedNews.value = Result.Loading()
        try {
            val response = repository.searchForNews(searchQuery, searchedNewsPage)
            _searchedNews.value = handleSearchForNewsResponse(response)
        } catch (e: Exception) {
            _searchedNews.value = Result.Error(e.message ?: "")
        }
    }

    private fun handleSearchForNewsResponse(response: Response<NewsResponse>): Result<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                searchForNewsResponse?.let {
                    val oldArticles = searchForNewsResponse.articles
                    val newArticles = newsResponse.articles
                    oldArticles.addAll(newArticles)
                }
                return Result.Success(searchForNewsResponse ?: newsResponse)
            }
        }
        return Result.Error(response.message())
    }

    fun getBreakingNews(countryCode: String = "us") = viewModelScope.launch {
        _breakingNews.value = Result.Loading()
        try {
            val response = repository.getBreakingNews(countryCode, breakingNewsPage)
            _breakingNews.value = handleBreakingNewsResponse(response)
        } catch (e: Exception) {
            _breakingNews.value = Result.Error(e.message ?: "")
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Result<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->
                breakingNewsResponse?.let {
                    val oldArticles = breakingNewsResponse.articles
                    val newArticles = newsResponse.articles
                    oldArticles.addAll(newArticles)
                }
                return Result.Success(breakingNewsResponse ?: newsResponse)
            }
        }
        return Result.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.saveArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun getSavedArticles() = repository.getSavedNews()

    fun clearSearchedNews() {
        val clearedSearchResult = Result.Success(NewsResponse(mutableListOf(), "", 0))
        _searchedNews.value = clearedSearchResult
    }
}