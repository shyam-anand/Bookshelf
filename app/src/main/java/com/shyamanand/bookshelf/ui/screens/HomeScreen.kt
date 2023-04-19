package com.shyamanand.bookshelf.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.shyamanand.bookshelf.R
import com.shyamanand.bookshelf.TAG
import com.shyamanand.bookshelf.data.model.ImageLinks
import com.shyamanand.bookshelf.data.model.SearchResult
import com.shyamanand.bookshelf.data.model.SearchResultItem
import com.shyamanand.bookshelf.data.model.Book
import com.shyamanand.bookshelf.ui.state.ResultsGridState
import com.shyamanand.bookshelf.ui.state.SearchbarState

@Composable
fun HomeScreen(
    searchbarState: SearchbarState,
    resultsGridState: ResultsGridState,
    onSearchStringChanged: (String) -> Unit,
    onBookSelected: (String) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Searchbar(
            searchbarState = searchbarState,
            onSearchStringChanged = onSearchStringChanged,
            onKeyboardDone = {},
            modifier = modifier,
        )
        when (resultsGridState) {
            is ResultsGridState.Success -> {
                Log.i("HomeScreen", "${resultsGridState.searchResult.totalItems} results")
                when (resultsGridState.searchResult.items.size) {
                    0 -> NoResults()
                    else -> SearchResultsGrid(
                        onPreviewClicked = onBookSelected,
                        searchResult = resultsGridState.searchResult,
                        modifier = modifier)
                }
            }
            is ResultsGridState.Loading -> LoadingScreen(modifier)
            is ResultsGridState.Error -> ErrorScreen(retryAction, modifier)
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )
    }
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.search_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun NoResults(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.no_results))
        Text(text = stringResource(R.string.no_results_caption))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Searchbar(
    searchbarState: SearchbarState,
    onSearchStringChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchbarState.searchString,
            onValueChange = onSearchStringChanged,
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardActions = KeyboardActions(
                onDone = { onKeyboardDone() }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            placeholder = {
                Text(text = stringResource(id = R.string.searchbox_placeholder))
            }
        )
    }
}

@Composable
fun SearchResultsGrid(
    onPreviewClicked: (String) -> Unit,
    searchResult: SearchResult,
    modifier: Modifier = Modifier
) {
    Log.i("HomeScreen", "Rendering search results")
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(
                R.string.searchresults_caption,
                "10", searchResult.totalItems.toString()
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = modifier.padding(8.dp).background(MaterialTheme.colorScheme.tertiaryContainer)
        )
        LazyColumn(
            contentPadding = PaddingValues(4.dp),
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            Log.i("HomeScreen", "Rendering results grid with ${searchResult.items.size} results")
            items(
                items = searchResult.items,
                key = { book -> book.id }
            ) {
                BookPreview(
                    searchResultItem = it,
                    onClick = { id -> run {
                        Log.d(TAG, "Clicked $id")
                        onPreviewClicked(id) } },
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun BookPreview(
    searchResultItem: SearchResultItem,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val book = searchResultItem.volumeInfo
    Log.i("HomeScreen","Rendering preview for ${book.title}")
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(searchResultItem.id) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            if (book.imageLinks.thumbnail != "") {
                Log.d("HomeScreen", "Loading thumbnail ${book.imageLinks.thumbnail}")
                AsyncImage(
                    model = book.imageLinks.thumbnail.replace("http:", "https:"),
                    contentDescription = stringResource(
                        R.string.book_preview_description,
                        book.title
                    ),
                    modifier = modifier
                        .padding(end = 16.dp)
                        .sizeIn(minHeight = 200.dp, maxHeight = 200.dp),
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.no_image),
                    alignment = Alignment.TopStart
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.book),
                    contentDescription = "",
                    modifier = modifier
                        .padding(end = 16.dp)
                        .sizeIn(minHeight = 200.dp, maxHeight = 200.dp)
                )
            }
            Column {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (book.subtitle.isNotEmpty()) {
                    Text(
                        text = book.subtitle,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (book.authors.isNotEmpty()) {
                    Text(
                        text = book.authors.joinToString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchbarPreview(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Searchbar(
            searchbarState = SearchbarState.Empty,
            onSearchStringChanged = { },
            onKeyboardDone = { })
    }
}

@Preview
@Composable
fun SearchResultsGridPreview(modifier: Modifier = Modifier) {
    val books = listOf(
        SearchResultItem(
            id = "IYaPEAAAQBAJ",
            selfLink = "https://www.googleapis.com/books/v1/volumes/IYaPEAAAQBAJ",
            volumeInfo = Book(
                title = "Sapiens",
                imageLinks = ImageLinks(
                    smallThumbnail = "http://books.google.com/books/content?id=1EiJAwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
                    thumbnail = "http://books.google.com/books/content?id=1EiJAwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
                )
            )
        ),
        SearchResultItem(
            id = "1Gd0QgAACAAJ",
            selfLink = "https://www.googleapis.com/books/v1/volumes/1Gd0QgAACAAJ",
            volumeInfo = Book(
                title =  "Mapi, Sapi, and Tapi",
                imageLinks = ImageLinks(
                    smallThumbnail = "http://books.google.com/books/content?id=1Gd0QgAACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api",
                    thumbnail = "http://books.google.com/books/content?id=1Gd0QgAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api"
                )
            )
        )
    )
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        SearchResultsGrid(onPreviewClicked = { id ->
            Log.d("HomeScreenPreview", "clicked $id") },
            searchResult = SearchResult(2, books)
        )
    }
}