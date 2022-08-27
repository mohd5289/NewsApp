package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.NewsActivity
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment:Fragment(R.layout.fragment_article) {

lateinit var viewModel: NewsViewModel
val args: ArticleFragmentArgs by navArgs()
    lateinit var binding: FragmentArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_article,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    viewModel = (activity as NewsActivity).viewModel
  val article = args.article
        binding.webView.apply {
    webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
}

binding.fab.setOnClickListener{
    viewModel.saveArticle(article)
    Snackbar.make(view,"Article saved successfully", Snackbar.LENGTH_SHORT).show()
}
    }


}