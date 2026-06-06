package ec.edu.puce.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.puce.githubclient.models.RepoRequest
import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.sevices.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoListViewModel : ViewModel() {
    private val _repos = MutableStateFlow<List<Repository>>(emptyList())
    val repos: StateFlow<List<Repository>> = _repos.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    init {
        fetchRepos()
    }

    fun fetchRepos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                _repos.value = RetrofitClient.apiService.getRepositories()
            } catch (e: Exception) {
                _errorMsg.value = "Error al cargar repositorios: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateRepository(repository: Repository, newName: String, newDescription: String) {
        viewModelScope.launch {
            try {
                val request = RepoRequest(newName, newDescription)
                val response = RetrofitClient.apiService.updateRepository(
                    repository.owner.login, repository.name, request
                )
                if (response.isSuccessful) {
                    val updatedRepo = response.body() ?: return@launch
                    _repos.value = _repos.value.map { 
                        if (it.id == repository.id) updatedRepo else it 
                    }
                } else {
                    _errorMsg.value = "Error al actualizar: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMsg.value = "Error: ${e.localizedMessage}"
            }
        }
    }

    fun deleteRepository(repository: Repository) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.deleteRepository(
                    repository.owner.login, repository.name
                )
                if (response.isSuccessful) {
                    _repos.value = _repos.value.filter { it.id != repository.id }
                } else {
                    _errorMsg.value = "Error al eliminar: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMsg.value = "Error: ${e.localizedMessage}"
            }
        }
    }
}
