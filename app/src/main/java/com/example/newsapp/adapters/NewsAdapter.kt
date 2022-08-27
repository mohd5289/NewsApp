package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemArticlePreviewBinding

import com.example.newsapp.models.Article
//import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

// val ivArticleImage = findViewById(R.id.ivArticleImage)
    inner  class ArticleViewHolder(val binding:ItemArticlePreviewBinding):RecyclerView.ViewHolder(binding.root){
    fun bind(article: Article) {
//       Glide.with(this)
// var bi =
        Glide.with(binding.root.context ).load(article.urlToImage).into(binding.ivArticleImage)
        binding.tvSource.text = article.source?.name
        binding.tvTitle.text = article.title
        binding.tvDescription.text= article.description
        binding.tvPublishedAt.text = article.publishedAt
        binding.executePendingBindings()
    }



    }
    private val differCallBack= object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return  oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem== newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        val binding =    ItemArticlePreviewBinding.inflate(layoutInflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
    return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

       holder.itemView.apply {
               setOnClickListener{
                   onItemClickListener?.let {
                       it(article)
                   }
               }

            }
      holder.bind(article)
        }


private var onItemClickListener:((Article)->Unit)?=null
    fun setOnItemClickListener(listener: (Article)->Unit){
        onItemClickListener = listener
    }
}