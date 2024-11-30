package my.android.githubsearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import my.android.githubsearch.data.model.Contributor
import my.android.githubsearch.data.model.Repository
import my.android.githubsearch.data.repository.GitHubRepository
import javax.inject.Inject

@HiltViewModel
class RepositoryDetailViewModel @Inject constructor(private val gitHubRepository: GitHubRepository) : ViewModel() {

    private val _repository = MutableStateFlow<Repository?>(null)
    val repository: StateFlow<Repository?> get() = _repository.asStateFlow()

    private val _contributors = MutableStateFlow<List<Contributor>>(emptyList())
    val contributors: StateFlow<List<Contributor>> get() = _contributors.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error.asStateFlow()

    fun loadRepositoryDetails(repository: Repository) {
        _error.value = null
        _contributors.value = emptyList()
        _repository.value = repository

        viewModelScope.launch {
            _isLoading.value = true
            try {
                _contributors.value = gitHubRepository.getRepositoryContributors(
                    repository.owner.login,
                    repository.name
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = when {
                    e.message?.contains("404", true) == true ->
                        "Repository not found. It may have been deleted or made private."
                    e.message?.contains("network", true) == true ->
                        "Network error. Please check your internet connection."
                    else -> "Failed to load contributors. Please try again later."
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retryLoadContributors() {
        _repository.value?.let { loadRepositoryDetails(it) }
    }
}