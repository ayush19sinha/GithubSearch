package my.android.githubsearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import my.android.githubsearch.data.model.Repository
import my.android.githubsearch.data.repository.GitHubRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val gitHubRepository: GitHubRepository) : ViewModel() {
    private val _repositories = MutableStateFlow<List<Repository>>(emptyList())
    val repositories: StateFlow<List<Repository>> = _repositories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var currentPage = 1
    private var currentQuery = ""
    private var hasMoreResults = true

    fun searchRepositories(query: String, page: Int = 1) {
        resetSearchState(query, page)

        fetchRepositories(query, page) { repos ->
            _repositories.value = repos
            hasMoreResults = repos.isNotEmpty()
        }
    }

    fun loadMoreRepositories() {
        if (currentQuery.isBlank() || !hasMoreResults || _isLoading.value) return
        fetchRepositories(currentQuery, ++currentPage) { newRepos ->
            if (newRepos.isEmpty()) hasMoreResults = false
            else _repositories.update { it + newRepos }
        }
    }

    private fun fetchRepositories(query: String, page: Int, onResult: (List<Repository>) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            gitHubRepository.searchRepositories(query, page)
                .catch { _error.value = it.message ?: "An unknown error occurred" }
                .collect { onResult(it) }
            _isLoading.value = false
        }
    }

    fun resetSearch() = resetSearchState()

    private fun resetSearchState(query: String = "", page: Int = 1) {
        currentQuery = query
        currentPage = page
        hasMoreResults = true
        _repositories.value = emptyList()
        _error.value = null
    }
}