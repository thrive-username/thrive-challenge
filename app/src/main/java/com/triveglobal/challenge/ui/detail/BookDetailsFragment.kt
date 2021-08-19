package com.triveglobal.challenge.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.triveglobal.challenge.ui.feed.BookFeedViewModel
import com.triveglobal.challenge.ui.utils.assistedViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BookDetailsFragment : DaggerFragment() {

    private val args by navArgs<BookDetailsFragmentArgs>()
    @Inject
    lateinit var viewModelFactory: BookDetailsViewModel.Factory
    private val viewModel: BookDetailsViewModel by assistedViewModel { viewModelFactory.create(args.book) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}