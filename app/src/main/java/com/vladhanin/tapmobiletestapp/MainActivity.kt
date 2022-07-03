package com.vladhanin.tapmobiletestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditTextView: EditText
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsRecyclerViewAdapter

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchEditTextView = findViewById(R.id.searchEditTextView)
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)

        resultsAdapter = ResultsRecyclerViewAdapter()
        resultsRecyclerView.apply {
            adapter = resultsAdapter
        }

        ViewModelProvider(this)
            .get(MainViewModel::class.java)
            .let {
                viewModel = it
            }
    }
}