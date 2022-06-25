package com.example.newsapp.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.model.Article
import com.example.newsapp.ui.screens.base.BaseFragment
import com.example.newsapp.util.Constants.Companion.TYPING_SEARCH_DELAY
import com.example.newsapp.util.Result.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchNewsBinding

    private var searchingJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(binding.rvSearchNews)

        observe()

        addTextChangedListener()

        newsAdapter.setOnNewsItemClickListener { article ->
            openArticle(article)
        }

        binding.errorCaseHolder.tryAgainButton.setOnClickListener {
            makeSearch(binding.etSearch.text.toString())
        }
    }

    private fun openArticle(article: Article) {
        val action = SearchFragmentDirections.actionSearchFragmentToArticleFragment(article)
        findNavController().navigate(action)
    }

    private fun addTextChangedListener() {
        binding.etSearch.addTextChangedListener { editable ->
            searchingJob?.cancel()
            searchingJob = MainScope().launch {
                delay(TYPING_SEARCH_DELAY)
                editable?.let {
                    makeSearch(editable.toString())
                }
            }
        }
    }

    private fun makeSearch(searchQuery: String) {
        if (searchQuery.isNotEmpty()) {
            viewModel.searchForNews(searchQuery)
        } else {
            viewModel.clearSearchedNews()
        }
    }

    private fun observe() {
        viewModel.searchedNews.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Success -> {
                    hideProgressBar(binding.paginationProgressBar)
                    newsAdapter.differ.submitList(resource.data?.articles)
                    binding.errorCaseHolder.root.visibility = View.GONE
                    binding.rvSearchNews.visibility = View.VISIBLE
                }
                is Error -> {
                    hideProgressBar(binding.paginationProgressBar)
                    binding.errorCaseHolder.root.visibility = View.VISIBLE
                    binding.rvSearchNews.visibility = View.GONE
                }
                is Loading -> {
                    showProgressBar(binding.paginationProgressBar)
                    binding.errorCaseHolder.root.visibility = View.GONE
                }
            }
        })
    }
}