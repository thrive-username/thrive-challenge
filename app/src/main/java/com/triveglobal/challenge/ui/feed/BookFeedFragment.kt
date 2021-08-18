package com.triveglobal.challenge.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
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
            setContent {
                Text(text = "Hello world.")
            }
        }
    }

}