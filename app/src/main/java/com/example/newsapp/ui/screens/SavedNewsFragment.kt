package com.example.newsapp.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.model.Article
import com.example.newsapp.ui.screens.base.BaseFragment
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : BaseFragment() {

    private lateinit var binding: FragmentSavedNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(binding.rvSavedNews)

        viewModel.getSavedArticles().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })

        newsAdapter.setOnNewsItemClickListener { article ->
            openArticle(article)
        }
        val itemTouchHelperCallback = createDeletingArticleItemTouchHelperCallback()
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }
    }

    private fun openArticle(article: Article) {
        val action =
            SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
        findNavController().navigate(action)
    }

    private fun createDeletingArticleItemTouchHelperCallback() =
        object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                view?.let { view ->
                    Snackbar.make(view, "Article successfully deleted", Snackbar.LENGTH_SHORT)
                        .apply {
                            setAction("Undo") {
                                viewModel.saveArticle(article)
                            }
                            show()
                        }
                }
            }
        }
}