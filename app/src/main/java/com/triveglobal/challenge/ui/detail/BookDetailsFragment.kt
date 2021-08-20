package com.triveglobal.challenge.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.triveglobal.challenge.R
import com.triveglobal.challenge.model.Book
import com.triveglobal.challenge.ui.common.LoadingDialog
import com.triveglobal.challenge.ui.feed.BookFeedUIModel
import com.triveglobal.challenge.ui.feed.BookFeedViewModel
import com.triveglobal.challenge.ui.theme.ChallengeTheme
import com.triveglobal.challenge.ui.utils.assistedViewModel
import dagger.android.support.DaggerFragment
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject

class BookDetailsFragment : DaggerFragment() {

    private val args by navArgs<BookDetailsFragmentArgs>()
    @Inject
    lateinit var viewModelFactory: BookDetailsViewModel.Factory
    private val viewModel: BookDetailsViewModel by assistedViewModel { viewModelFactory.create(args.book) }
    private val dateFormatter = DateTimeFormat.forPattern("MMMM d, yyyy h:mma")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent { DetailContent(viewModel, dateFormatter, findNavController()) }
        }
    }

    @Composable
    private fun DetailContent(viewModel: BookDetailsViewModel, dateFormatter: DateTimeFormatter, navController: NavController) {
        viewModel.uiModelLiveData.observeAsState().value?.let { uiModel ->
            RenderUIModel(uiModel, dateFormatter){
                navController.popBackStack()
            }
        }
    }

    @Composable
    private fun RenderUIModel(uiModel: BookDetailsUIModel, dateFormatter: DateTimeFormatter, onBackClicked: () -> Unit){
        ChallengeTheme {
            Column(Modifier.fillMaxHeight()) {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.book_details_tile)) },
                    navigationIcon = {
                        Image(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back),
                            modifier = Modifier.clickable { onBackClicked() }
                        )
                    },
                    actions = {

                    }
                )
                BookDetails(uiModel.book, dateFormatter)
            }
            if (uiModel.loading){
                LoadingDialog(message = stringResource(id = R.string.book_details_check_out_message))
            }
            if (uiModel.displaySuccessMessage){
                TODO()
            }
        }
    }

    @Composable
    private fun BookDetails(book: Book, dateFormatter: DateTimeFormatter){
        val scrollState = rememberScrollState()
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(20.dp)
                .scrollable(orientation = Orientation.Vertical, state = scrollState)) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.h1.copy(textAlign = TextAlign.Start)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = book.author,
                style = MaterialTheme.typography.h2.copy(textAlign = TextAlign.Start)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.book_details_publisher_template, book.publisher),
                style = MaterialTheme.typography.h2.copy(textAlign = TextAlign.Start)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.book_details_categories_template, book.categories),
                style = MaterialTheme.typography.h2.copy(textAlign = TextAlign.Start)
            )
            if (book.lastCheckedOutBy!= null && book.lastCheckedOut != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.book_details_checkout_template, book.lastCheckedOutBy, dateFormatter.print(book.lastCheckedOut)),
                    style = MaterialTheme.typography.h2.copy(textAlign = TextAlign.Start)
                )
            }
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        val book = Book("Author", "Categories", 1, null, null, "Publisher", "Title")
        RenderUIModel(BookDetailsUIModel(book, null, false, false), dateFormatter, {})
    }

}