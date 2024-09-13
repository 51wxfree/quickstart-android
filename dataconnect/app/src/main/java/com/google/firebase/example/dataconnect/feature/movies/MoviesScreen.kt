package com.google.firebase.example.dataconnect.feature.movies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.firebase.example.dataconnect.R
import com.google.firebase.example.dataconnect.data.Movie

@Composable
fun MoviesScreen(
    onMovieClicked: (movie: String) -> Unit,
    moviesViewModel: MoviesViewModel = viewModel()
) {
    val movies by moviesViewModel.uiState.collectAsState()
    MoviesScreen(movies, onMovieClicked)
}

@Composable
fun MoviesScreen(
    uiState: MoviesUIState,
    onMovieClicked: (movie: String) -> Unit
) {
    when (uiState) {
        MoviesUIState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
        is MoviesUIState.Error -> {
            Text(uiState.errorMessage)
        }
        is MoviesUIState.Success -> {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier.padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = stringResource(R.string.title_top_10_movies),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                HorizontalMovieList(uiState.top10movies, onMovieClicked)
                Text(
                    text = stringResource(R.string.title_latest_movies),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                HorizontalMovieList(uiState.latestMovies, onMovieClicked)
            }
        }
    }
}

@Composable
fun HorizontalMovieList(
    movies: List<Movie>,
    onMovieClicked: (movie: String) -> Unit
) {
    LazyRow {
        items(movies) { movie ->
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillParentMaxWidth(0.4f)
                    .clickable {
                        onMovieClicked(movie.id.toString())
                    },
            ) {
                AsyncImage(
                    model = movie.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
                )
                Text(
                    text = "Rating: ${movie.rating}",
                    modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}