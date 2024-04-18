package com.ksc.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ksc.newsapp.databinding.ActivityNewsBinding
import com.ksc.newsapp.R
import com.ksc.newsapp.db.ArticleDatabase
import com.ksc.newsapp.repository.NewsRepository
import com.ksc.newsapp.ui.fragments.FavouritesFragment
import com.ksc.newsapp.ui.fragments.HeadlinesFragment
import com.ksc.newsapp.ui.fragments.SearchNewsFragment

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    lateinit var newsViewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelFactory = NewsViewModelFactory(newsRepository)
        newsViewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.headline_fragment -> replaceFragments(HeadlinesFragment())
                R.id.favourite_fragment -> replaceFragments(FavouritesFragment())
                R.id.search_fragment -> replaceFragments(SearchNewsFragment())
                else -> {
                }
            }
            true
        }

    }

    fun replaceFragments(fragment: Fragment) {
        val fragmentsManager = supportFragmentManager
        val fragmentsTransaction = fragmentsManager.beginTransaction()
        fragmentsTransaction.replace(R.id.newsNavHostFragment, fragment)
        fragmentsTransaction.commit()

    }
}

