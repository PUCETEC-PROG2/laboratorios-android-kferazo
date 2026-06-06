package ec.edu.puce.githubclient.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.puce.githubclient.models.Repository
import ec.edu.puce.githubclient.viewmodels.RepoListViewModel

@Composable
fun RepoList(
    modifier: Modifier = Modifier,
    viewModel: RepoListViewModel = viewModel()
) {
    val repos by viewModel.repos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMsg by viewModel.errorMsg.collectAsState()

    var repoToEdit by remember { mutableStateOf<Repository?>(null) }
    var repoToDelete by remember { mutableStateOf<Repository?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        if (isLoading && repos.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        errorMsg?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }

        // --- Diálogo de Confirmación de Eliminación ---
        repoToDelete?.let { repo ->
            AlertDialog(
                onDismissRequest = { repoToDelete = null },
                title = { Text("¿Eliminar repositorio?") },
                text = { Text("Esta acción eliminará el repositorio '${repo.name}' permanentemente de GitHub.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteRepository(repo)
                            repoToDelete = null
                        }
                    ) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { repoToDelete = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // --- Diálogo de Edición ---
        repoToEdit?.let { repo ->
            var newName by remember { mutableStateOf(repo.name) }
            var newDesc by remember { mutableStateOf(repo.description ?: "") }

            AlertDialog(
                onDismissRequest = { repoToEdit = null },
                title = { Text("Editar Repositorio") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newDesc,
                            onValueChange = { newDesc = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.updateRepository(repo, newName, newDesc)
                            repoToEdit = null
                        }
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { repoToEdit = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (repos.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(repos) { repo ->
                    RepoItem(
                        repository = repo,
                        onEdit = { repoToEdit = repo },
                        onDelete = { repoToDelete = repo }
                    )
                }
            }
        }
    }
}
