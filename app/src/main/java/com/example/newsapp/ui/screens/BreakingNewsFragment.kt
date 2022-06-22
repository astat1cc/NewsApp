package com.example.newsapp.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.model.Article
import com.example.newsapp.ui.screens.base.BaseFragment
import com.example.newsapp.util.Result.*

class BreakingNewsFragment : BaseFragment() {

    private lateinit var binding: FragmentBreakingNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(binding.rvBreakingNews)

        observe()

        viewModel.getBreakingNews()

        newsAdapter.setOnNewsItemClickListener { article ->
            openArticle(article)
        }

        binding.errorCaseHolder.tryAgainButton.setOnClickListener {
            viewModel.getBreakingNews()
        }
    }

    private fun openArticle(article: Article) {
        val action =
            BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(article)
        findNavController().navigate(action)
    }

    private fun observe() {
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Success -> {
                    hideProgressBar(binding.paginationProgressBar)
                    newsAdapter.differ.submitList(resource.data?.articles)
                    binding.rvBreakingNews.visibility = View.VISIBLE
                    binding.errorCaseHolder.root.visibility = View.GONE
                }
                is Error -> {
                    hideProgressBar(binding.paginationProgressBar)
                    binding.rvBreakingNews.visibility = View.GONE
                    binding.errorCaseHolder.root.visibility = View.VISIBLE
                }
                is Loading -> {
                    showProgressBar(binding.paginationProgressBar)
                    binding.errorCaseHolder.root.visibility = View.GONE
                }
            }
        })
    }
}