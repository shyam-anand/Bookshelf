package com.shyamanand.bookshelf.ui.screens

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.shyamanand.bookshelf.R
import com.shyamanand.bookshelf.TAG
import com.shyamanand.bookshelf.data.model.ImageLinks
import com.shyamanand.bookshelf.data.model.Book
import com.shyamanand.bookshelf.ui.state.BookDetailsScreenState

@Composable
fun BookDetailScreen(
    state: BookDetailsScreenState,
    modifier: Modifier = Modifier
) {
    when (state) {
        is BookDetailsScreenState.Loading -> LoadingScreen(modifier)
        is BookDetailsScreenState.Error -> ErrorCard(
            error = R.string.something_went_wrong,
            modifier
        )
        is BookDetailsScreenState.Success -> BookDetailsCard(state.book, modifier)
    }
}

private fun getImageLink(imageLinks: ImageLinks): String {
    return if (imageLinks.extraLarge.isNotEmpty()) {
        Log.d(TAG, "Using extraLarge")
        imageLinks.extraLarge
    } else if (imageLinks.large.isNotEmpty()) {
        Log.d(TAG, "Using large")
        imageLinks.large
    } else if (imageLinks.medium.isNotEmpty()) {
        Log.d(TAG, "Using medium")
        imageLinks.medium
    } else if (imageLinks.small.isNotEmpty()) {
        Log.d(TAG, "Using small")
        imageLinks.small
    } else {
        ""
    }
}

@Composable
fun BookDetailsCard(book: Book, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val imageLink = getImageLink(book.imageLinks)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = modifier.background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Spacer(modifier = modifier.fillMaxWidth().size(16.dp))
            if (imageLink.isNotEmpty()) {
                Log.d(TAG, "Loading $imageLink")
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(imageLink.replace("http:", "https:"))
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    error = painterResource(R.drawable.no_image),
                    placeholder = painterResource(R.drawable.loading_img),
                    modifier = modifier
                        .align(Alignment.CenterHorizontally)
                        .size(250.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Log.d(TAG, "No imageLink found for ${book.title}")
                Image(
                    painter = painterResource(id = R.drawable.book),
                    contentDescription = "",
                    modifier = modifier.size(250.dp)
                )
            }

            Text(
                text = book.title,
                style = MaterialTheme.typography.displaySmall
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
        Text(
            text = book.description,
            style = MaterialTheme.typography.bodySmall,
            modifier = modifier.padding(8.dp)
        )
    }
}

@Composable
fun ErrorCard(@StringRes error: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(error),
            style = MaterialTheme.typography.displayLarge
        )
    }
}

@Preview
@Composable
fun BookDetailScreenPreview(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        BookDetailsCard(book = Book(
            title = "Book Title",
            subtitle = "Read this book",
            authors = listOf("Shyam Anand", "HeavyDistortion"),
            description = "This is a book. It contains sentences made using words, that are themselves made of letters."
        )
        )
        
    }
}