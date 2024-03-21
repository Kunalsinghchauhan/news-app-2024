package com.ksc.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ksc.newsapp.R
import com.ksc.newsapp.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_error, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        articleTitle = holder.itemView.findViewById(R.id.tv_article_title)
        articleSource = holder.itemView.findViewById(R.id.tv_article_source)
        articleDescription = holder.itemView.findViewById(R.id.tv_article_description)
        articleDateTime = holder.itemView.findViewById(R.id.tv_article_date_time)
        articleImage = holder.itemView.findViewById(R.id.iv_article_image)
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(articleImage)
            articleSource.text = article.source?.name
            articleTitle.text = article.title
            articleDescription.text = article.description
            articleDateTime.text = article.publishedAt
            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener

    }

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var articleImage: ImageView
    private lateinit var articleSource: TextView
    private lateinit var articleTitle: TextView
    private lateinit var articleDescription: TextView
    private lateinit var articleDateTime: TextView


    private val differCallBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallBack)
}