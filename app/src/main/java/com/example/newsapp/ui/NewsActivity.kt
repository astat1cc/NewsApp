package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.NewsAdapter
import com.example.newsapp.databinding.ActivityNewsBinding
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.repository.NewsRepository

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    private lateinit var repository: NewsRepository
    val newsViewModel: NewsViewModel by viewModels { factory(repository) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = NewsRepository(ArticleDatabase(this))

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)
    }
}