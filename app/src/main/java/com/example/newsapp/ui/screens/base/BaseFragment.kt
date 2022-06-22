package com.example.newsapp.ui.screens.base

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsAdapter
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.NewsViewModel

open class BaseFragment : Fragment() {

    lateinit var viewModel: NewsViewModel
    val newsAdapter = NewsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).newsViewModel
    }

    fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
    }

    fun setupRecyclerView(recyclerView: RecyclerView) {
        with(recyclerView) {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
        }
    }
}