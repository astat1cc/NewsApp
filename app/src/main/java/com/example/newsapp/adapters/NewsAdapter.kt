package com.example.newsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemArticlePreviewBinding
import com.example.newsapp.model.Article

typealias OnNewsItemClickListener = ((Article) -> Unit)

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private var onNewsItemClickListener: OnNewsItemClickListener? = null

    class ArticleViewHolder(val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticlePreviewBinding.inflate(inflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        with(holder.binding) {
            tvSource.text = article.source?.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt
            Glide.with(ivArticleImage)
                .load(article.urlToImage)
                .into(ivArticleImage)
            holder.itemView.setOnClickListener{
                onNewsItemClickListener?.let { it(article) }
            }
        }
    }

    fun setOnNewsItemClickListener(listener: OnNewsItemClickListener) {
        onNewsItemClickListener = listener
    }

    override fun getItemCount(): Int = differ.currentList.size
}