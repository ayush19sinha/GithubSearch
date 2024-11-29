package my.android.githubsearch.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import my.android.githubsearch.data.model.Repository
import my.android.githubsearch.data.model.RepositoryOwner
import my.android.githubsearch.ui.screens.components.RepositoryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
   onRepositoryClick: (Repository) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val isLoading by remember { mutableStateOf(false) }

    Scaffold(
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
            SearchBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onSearch = { },
                isLoading = isLoading
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (repositories.isEmpty()) {
                EmptyResult()
            } else {
                RepositoryList(
                    repositories = repositories,
                    onRepositoryClick = onRepositoryClick
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isLoading: Boolean
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        label = { Text("Search GitHub Repositories") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (!isLoading) {
                    onSearch()
                    keyboardController?.hide()
                }
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        trailingIcon = {
            IconButton(
                onClick = {
                    onSearch()
                    keyboardController?.hide()
                },
                enabled = !isLoading
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}

@Composable
private fun RepositoryList(
    repositories: List<Repository>,
    onRepositoryClick: (Repository) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(repositories) { repo ->
            RepositoryCard(
                repository = repo,
                onClick = { onRepositoryClick(repo) },
                modifier = Modifier.fillMaxWidth()
            )
        }
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

val repositories = listOf (
    Repository(
        id = 123456,
        name = "LearnKotlin",
        full_name = "ayush/learn-kotlin",
        description = "This is the best repository to learn Kotlin.",
        html_url = "https://github.com/ayush/learn-kotlin",
        stargazers_count = 1500,
        language = "Kotlin",
        forks_count = 250,
        updated_at = "24/11/24",
        watchers_count = 2423,
        open_issues_count = 45,
        isOfflineCache = false,
        owner = RepositoryOwner(
            avatar_url = "https://avatars.githubusercontent.com/u/143383811?v=4",
            html_url = "https://github.com/ayush19sinha",
            login = "Ayush"
        )
    ),
    Repository(
        id = 321321,
        name = "LearnJava",
        full_name = "ayush/learn-Java",
        description = "This is the best repository to learn Java.",
        html_url = "https://github.com/ayush/learn-Java",
        stargazers_count = 1837,
        language = "Java",
        forks_count = 290,
        updated_at = "24/11/24",
        watchers_count = 141,
        open_issues_count = 123,
        isOfflineCache = false,
        owner = RepositoryOwner(
            avatar_url = "",
            html_url = "https://github.com/ayush19sinha",
            login = "Ayush"
        )
    ),
)

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(onRepositoryClick = {})
    }
}