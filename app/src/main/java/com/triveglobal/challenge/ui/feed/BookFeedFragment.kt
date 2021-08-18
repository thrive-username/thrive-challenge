package com.triveglobal.challenge.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import com.triveglobal.challenge.ui.utils.assistedViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BookFeedFragment : DaggerFragment(){

    @Inject
    lateinit var viewModelFactory: BookFeedViewModel.Factory
    private val viewModel: BookFeedViewModel by assistedViewModel { viewModelFactory.create(it) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent { feedContent() }
        }
    }

    @Preview
    @Composable
    private fun feedContent(){
        val uiModel by viewModel.uiModelLiveData.observeAsState(BookFeedUIModel(emptyList(), false, null))
        LazyColumn {
            items(uiModel.items) {
                Text("Hello World")
            }
        }
    }

}