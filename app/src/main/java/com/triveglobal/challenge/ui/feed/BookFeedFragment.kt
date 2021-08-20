package com.triveglobal.challenge.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.triveglobal.challenge.R
import com.triveglobal.challenge.model.Book
import com.triveglobal.challenge.ui.common.ErrorDialog
import com.triveglobal.challenge.ui.theme.Black
import com.triveglobal.challenge.ui.theme.ChallengeTheme
import com.triveglobal.challenge.ui.theme.Gray
import com.triveglobal.challenge.ui.theme.White
import com.triveglobal.challenge.ui.utils.assistedViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class BookFeedFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: BookFeedViewModel.Factory
    private val viewModel: BookFeedViewModel by assistedViewModel { viewModelFactory.create() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent { FeedContent(viewModel, findNavController()) }
        }
    }

    @Composable
    private fun FeedContent(viewModel: BookFeedViewModel, navController: NavController){
        viewModel.uiModelLiveData.observeAsState().value?.let { uiModel ->
            RenderUIModel(uiModel, { navController.navigate(BookFeedFragmentDirections.actionFeedToDetails(it)) }, { viewModel.synchronize() })
        }
    }

    @Composable
    private fun RenderUIModel(uiModel: BookFeedUIModel, onBookClicked: (Book) -> Unit, onSynchronizedClicked: () -> Unit) {
        ChallengeTheme {
            Column(Modifier.fillMaxHeight()) {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.book_feed_title)) },
                    actions = {
                        if (uiModel.loading) {
                            LoadingIndicator()
                        }else{
                            Image(
                                painter = painterResource(R.drawable.ic_sync),
                                contentDescription = stringResource(R.string.book_feed_synchronize),
                                modifier = Modifier.clickable { onSynchronizedClicked() }
                            )
                        }
                    }
                )
                when {
                    uiModel.items == null -> OnScreenMessage(stringResource(R.string.book_feed_loading_content))
                    uiModel.items.isEmpty() -> OnScreenMessage(stringResource(R.string.book_feed_empty_message))
                    else -> BookFeed(uiModel.items, onBookClicked)
                }
            }
        }
        uiModel.error?.let {
            ErrorDialog(
                it,
                onDismiss = { viewModel.onErrorDismissed() })
        }
    }

    @Composable
    private fun BookFeed(books: List<Book>, onBookClicked: (Book) -> Unit) {
        LazyColumn {
            items(books) {
                BookShortSummary(it) { book -> onBookClicked(book) }
            }
        }
    }

    @Composable
    private fun OnScreenMessage(message: String) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.h2.copy(textAlign = TextAlign.Center),
                color = Gray
            )
        }
    }


    @Composable
    private fun LoadingIndicator() {
        CircularProgressIndicator(
            color = White,
            modifier = Modifier
                .width(30.dp)
                .height(30.dp),
            strokeWidth = 2.dp
        )
    }

    @Composable
    private fun BookShortSummary(book: Book, onClick: (Book) -> Unit) {
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(book) }) {
            val (title, author) = createRefs()
            Text(
                text = book.title,
                style = MaterialTheme.typography.h1.copy(textAlign = TextAlign.Start),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, 10.dp)
                        start.linkTo(parent.start, 10.dp)
                        end.linkTo(parent.end, 10.dp)
                        width = Dimension.fillToConstraints
                    }
            )
            Text(
                text = stringResource(id = R.string.book_feed_author_template, book.author),
                style = MaterialTheme.typography.h2.copy(textAlign = TextAlign.Start),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(author) {
                        top.linkTo(title.bottom, 5.dp)
                        start.linkTo(title.start)
                        end.linkTo(title.end)
                        width = Dimension.fillToConstraints
                        bottom.linkTo(parent.bottom, 10.dp)
                    }
            )
            Divider(color = Black, thickness = 1.dp)
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        val book = Book("Author", "Categories", 1, null, null, "Publisher", "Title")
        val books = listOf(book, book, book)
        RenderUIModel(uiModel = BookFeedUIModel(books, true, null), {}, {})
    }

}