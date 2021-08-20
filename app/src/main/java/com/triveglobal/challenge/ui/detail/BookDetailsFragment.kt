package com.triveglobal.challenge.ui.detail

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
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.triveglobal.challenge.R
import com.triveglobal.challenge.model.Book
import com.triveglobal.challenge.ui.common.LoadingDialog
import com.triveglobal.challenge.ui.theme.Black
import com.triveglobal.challenge.ui.theme.ChallengeTheme
import com.triveglobal.challenge.ui.theme.White
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
    private fun DetailContent(
        viewModel: BookDetailsViewModel,
        dateFormatter: DateTimeFormatter,
        navController: NavController
    ) {
        val showCheckoutDialog = remember { mutableStateOf(false) }
        viewModel.uiModelLiveData.observeAsState().value?.let { uiModel ->
            RenderUIModel(
                uiModel,
                dateFormatter,
                {
                    showCheckoutDialog.value = true
                },
                {
                    navController.popBackStack()
                })
        }
        if (showCheckoutDialog.value) {
            CheckoutDialog(
                {
                    showCheckoutDialog.value = false
                }, {
                    viewModel.checkOut(it)
                }
            )
        }
    }

    @Composable
    private fun RenderUIModel(
        uiModel: BookDetailsUIModel,
        dateFormatter: DateTimeFormatter,
        checkoutClicked: () -> Unit,
        onBackClicked: () -> Unit
    ) {
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
                        //TODO: Add share button
                    }
                )
                BookDetails(uiModel.book, Modifier.weight(1F), dateFormatter)
                Button(
                    onClick = { checkoutClicked() },
                    modifier = Modifier.align(CenterHorizontally)
                ) {
                    Text(text = stringResource(R.string.book_details_checkout_button))
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
            if (uiModel.loading) {
                LoadingDialog(message = stringResource(id = R.string.book_details_check_out_message))
            }
            if (uiModel.displaySuccessMessage) {

            }
        }
    }

    @Composable
    private fun CheckoutDialog(onDismiss: () -> Unit, onSubmit: (String) -> Unit) {
        val checkoutName = remember { mutableStateOf("") }
        ChallengeTheme {
            Dialog(
                onDismissRequest = { onDismiss() },
                properties = DialogProperties(),
                content = {
                    Surface(color = White) {
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                style = MaterialTheme.typography.h1,
                                text = stringResource(R.string.book_details_checkout_dialog_title)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            TextField(
                                value = checkoutName.value,
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    color = Black
                                ),
                                modifier = Modifier.focusTarget(),
                                onValueChange = { checkoutName.value = it }
                            )
                            Button(
                                onClick = { onSubmit(checkoutName.value) },
                                modifier = Modifier.align(CenterHorizontally),
                                enabled = checkoutName.value.isNotEmpty()
                            ) {
                                Text(text = stringResource(R.string.submit))
                            }
                        }
                    }
                }
            )
        }
    }

    @Composable
    private fun BookDetails(book: Book, modifier: Modifier, dateFormatter: DateTimeFormatter) {
        val scrollState = rememberScrollState()
        Column(
            modifier
                .fillMaxWidth()
                .padding(20.dp)
                .scrollable(orientation = Orientation.Vertical, state = scrollState)
        ) {
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
            if (book.lastCheckedOutBy != null && book.lastCheckedOut != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        R.string.book_details_checkout_template,
                        book.lastCheckedOutBy,
                        dateFormatter.print(book.lastCheckedOut)
                    ),
                    style = MaterialTheme.typography.h2.copy(textAlign = TextAlign.Start)
                )
            }
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        val book = Book("Author", "Categories", 1, null, null, "Publisher", "Title")
        RenderUIModel(BookDetailsUIModel(book, null, false, false), dateFormatter, {}, {})
    }
}