package com.shyamanand.bookshelf.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shyamanand.bookshelf.ui.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        val bookshelfAppViewModel: BookshelfAppViewModel = viewModel()

        val homeScreenViewModel: HomeScreenViewModel = viewModel(
            factory = HomeScreenViewModel.Factory)

        val bookDetailsScreenViewModel: BookDetailsScreenViewModel = viewModel(
            factory = BookDetailsScreenViewModel.Factory)

        val uiState by bookshelfAppViewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = BookShelfAppScreen.Home.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(BookShelfAppScreen.Home.name) {
                HomeScreen(
                    searchbarState = homeScreenViewModel.searchbarState,
                    resultsGridState = homeScreenViewModel.resultsGridState,
                    onSearchStringChanged = { searchString ->
                        homeScreenViewModel.onSearchbarInput(searchString)
                    },
                    onBookSelected = { bookId ->
                        bookshelfAppViewModel.setBookDetailsScreen(bookId)
                        bookDetailsScreenViewModel.loadBook(bookId)
                        navController.navigate(BookShelfAppScreen.BookDetails.name) },
                    retryAction = { homeScreenViewModel.search() },
                    modifier = modifier
                )
            }
            composable(BookShelfAppScreen.BookDetails.name) {
                val book = uiState.bookId
                if (book != null) {
                    BookDetailScreen(
                        state = bookDetailsScreenViewModel.state,
                        modifier = modifier
                    )
                } else {
                    throw IllegalStateException(
                        "uiState.book cannot be null when " +
                                "uiState.screen == BookShelfAppScreen.BookDetails")
                }
            }
        }
    }
}