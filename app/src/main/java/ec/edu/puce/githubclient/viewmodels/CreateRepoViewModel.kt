package ec.edu.puce.githubclient.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.puce.githubclient.models.RepoRequest
import ec.edu.puce.githubclient.sevices.RetrofitClient
import kotlinx.coroutines.launch

class CreateRepoViewModel : ViewModel() {
    var name by mutableStateOf("")
    var description by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var message by mutableStateOf("")

    fun createRepo(onSuccess: () -> Unit) {
        if (name.isBlank()) {
            message = "Error: El nombre es obligatorio"
            return
        }

        viewModelScope.launch {
            isLoading = true
            message = ""
            try {
                val request = RepoRequest(name, description)
                val response = RetrofitClient.apiService.createRepository(request)
                
                if (response.isSuccessful) {
                    message = "¡Éxito!"
                    name = ""
                    description = ""
                    onSuccess() // Regresa a la lista
                } else {
                    message = "Error: ${response.code()} ${response.message()}"
                }
            } catch (e: Exception) {
                message = "Error: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}
