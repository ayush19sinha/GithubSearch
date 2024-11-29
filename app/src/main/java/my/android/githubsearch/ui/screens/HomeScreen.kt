package my.android.githubsearch.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import my.android.githubsearch.data.model.Repository
import my.android.githubsearch.ui.screens.components.AppSearchBar
import my.android.githubsearch.ui.screens.components.RepositoryCard
import my.android.githubsearch.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onRepositoryClick: (Repository) -> Unit
) {
    val repositories by homeViewModel.repositories.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val error by homeViewModel.error.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()
    var hasSearched by remember { mutableStateOf(false) }

    LaunchedEffect(error) {
        error?.let { errorMessage ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(errorMessage)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("GitHub Repository Search") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AppSearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearch = {
                    hasSearched = true
                    homeViewModel.searchRepositories(searchQuery) },
                onCrossClick = {
                    searchQuery = ""
                    hasSearched = false
                    homeViewModel.resetSearch()
                },
                isLoading = isLoading
            )

            if (isLoading && repositories.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                RepositoryList(
                    repositories = repositories,
                    listState = listState,
                    isLoading = isLoading,
                    hasSearched = hasSearched,
                    onRepositoryClick = onRepositoryClick,
                    onLoadMore = { homeViewModel.loadMoreRepositories() }
                )
            }
        }
    }
}

@Composable
private fun RepositoryList(
    repositories: List<Repository>,
    listState: LazyListState,
    isLoading: Boolean,
    hasSearched: Boolean,
    onRepositoryClick: (Repository) -> Unit,
    onLoadMore: () -> Unit
) {
    when {
        repositories.isEmpty() && !isLoading && !hasSearched -> {
            InitialMessage()
        }
        repositories.isEmpty() && !isLoading -> {
            EmptyResult()
        }
        else -> {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(repositories) { repo ->
                    RepositoryCard(
                        repository = repo,
                        onClick = { onRepositoryClick(repo) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo }
                    .collect { layoutInfo ->
                        val totalItems = layoutInfo.totalItemsCount
                        val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                        if (lastVisibleItemIndex >= totalItems - 5 && !isLoading) {
                            onLoadMore()
                        }
                    }
            }
        }
    }
}

@Composable
private fun InitialMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Welcome!\nSearch for repositories to get started.",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EmptyResult() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No repositories found.\nTry a different search query.",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}