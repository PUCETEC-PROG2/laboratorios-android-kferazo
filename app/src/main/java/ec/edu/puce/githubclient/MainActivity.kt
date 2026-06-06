package ec.edu.puce.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.puce.githubclient.ui.CreateRepoScreen
import ec.edu.puce.githubclient.ui.components.RepoList
import ec.edu.puce.githubclient.ui.theme.GithubClientTheme
import ec.edu.puce.githubclient.viewmodels.RepoListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubClientTheme {
                // Hoist the RepoListViewModel to refresh it when needed
                val repoListViewModel: RepoListViewModel = viewModel()
                var showForm by remember { mutableStateOf(false) }

                Scaffold(
                    floatingActionButton = {
                        if (!showForm) {
                            FloatingActionButton(
                                onClick = { showForm = true },
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Crear Repositorio")
                            }
                        }
                    }
                ) { padding ->
                    if (showForm) {
                        CreateRepoScreen(onBack = { 
                            showForm = false
                            // Refresh the list when returning from the form
                            repoListViewModel.fetchRepos()
                        })
                    } else {
                        RepoList(
                            modifier = Modifier.padding(padding),
                            viewModel = repoListViewModel
                        )
                    }
                }
            }
        }
    }
}
