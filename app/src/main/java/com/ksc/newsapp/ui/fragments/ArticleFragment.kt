package com.ksc.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ksc.newsapp.R
import com.ksc.newsapp.databinding.FragmentArticleBinding
import com.ksc.newsapp.ui.NewsActivity
import com.ksc.newsapp.ui.NewsViewModel


class ArticleFragment : Fragment(R.layout.fragment_article) {
    private lateinit var viewModel: NewsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).newsViewModel
    }

}