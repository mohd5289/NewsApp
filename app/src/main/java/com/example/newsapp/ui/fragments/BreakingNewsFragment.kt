package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsActivity
import com.example.newsapp.NewsApplication
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.ui.NewsViewModel
import com.example.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapp.util.Resource


class BreakingNewsFragment:Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
lateinit var newsAdapter: NewsAdapter
lateinit var binding: FragmentBreakingNewsBinding
val TAG = "BreakingNewsFragment"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

    binding=    DataBindingUtil.inflate<FragmentBreakingNewsBinding>(inflater, R.layout.fragment_breaking_news,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        DataBindingUtil.inflate<FragmentBreakingNewsBinding>(layoutInflater, R.layout.fragment_breaking_news,false)
        viewModel = (activity as NewsActivity).viewModel
        binding.rvBreakingNews.setUp()

    newsAdapter.setOnItemClickListener {
        if (findNavController().currentDestination?.id == R.id.breakingNewsFragment2) {
            findNavController().navigate(
                BreakingNewsFragmentDirections.actionBreakingNewsFragment2ToArticleFragment3(
                    it
                )
            )
        }
        val bundle=Bundle().apply {
            putSerializable("article",it)
        }


    }
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success->{
                    hideProgressBar()
                    it.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                    val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if(isLastPage) {
                            binding.apply {
                                rvBreakingNews.setPadding(0, 0, 0, 0)
                            }
                        }
                    }

                }
                is Resource.Error->{
                    hideProgressBar()
                    it.message?.let { message->
                       Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_SHORT).show()
                    }

                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }
        })

    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading= false
    }
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    val scrollListener= object :RecyclerView.OnScrollListener(){


        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
            isScrolling = true
        }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
           val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition +visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition>=0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

       val shouldPaginate =     isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling


         if (shouldPaginate){
             viewModel.getBreakingNews("us")
             isScrolling = false
         }
        }
    }
    private fun RecyclerView.setUp(){
    newsAdapter = NewsAdapter()
    adapter= newsAdapter
    layoutManager = LinearLayoutManager(activity)
        addOnScrollListener(this@BreakingNewsFragment.scrollListener)

}

}