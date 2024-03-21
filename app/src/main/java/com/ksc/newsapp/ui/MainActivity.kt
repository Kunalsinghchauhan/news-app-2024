package com.ksc.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ksc.newsapp.R
import com.ksc.newsapp.databinding.ActivityMainBinding
import com.ksc.newsapp.db.ArticleDatabase
import com.ksc.newsapp.repository.Repository

class MainActivity : AppCompatActivity() {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val newsRepository = Repository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelFactory(application, newsRepository)
        newsViewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController= navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}