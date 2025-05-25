package com.kacperkk.postsapp.ui.screens.userdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kacperkk.postsapp.model.User
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kacperkk.postsapp.ui.components.MapItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    navController: NavController,
    user: User?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = (user?.name ?: "Profile"),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (user == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Użytkownik nie znaleziony")
            }
        } else {
            val viewModel: UserDetailViewModel = viewModel(
                factory = UserDetailViewModel.provideFactory(user.id)
            )
            val uiState by viewModel.uiState.collectAsState()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    Column {
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "@${user.username}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("📧 ${user.email}")
                        Text("📞 ${user.phone}")
                        Text("🌐 ${user.website}")
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "🏠 Adres:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("${user.address.street}, ${user.address.suite}")
                        Text("${user.address.city}, ${user.address.zipcode}")
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "🏢 Firma:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(user.company.name, fontWeight = FontWeight.Bold)
                        Text(user.company.catchPhrase)
                        Text(user.company.bs)

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                item {
                    Column {
                        Text(
                            text = "🌐 Mapa:",
                            style = MaterialTheme.typography.titleMedium
                        )

                        MapItem(
                            user.address.geo.lat.toDouble(),
                            user.address.geo.lng.toDouble(),
                            user.address.street,
                            user.address.suite,
                            modifier =  Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "📝 Zadania:",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                when (uiState) {
                    is UserDetailViewModel.UIState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                    is UserDetailViewModel.UIState.Error -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Błąd ładowania zadań.",
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { viewModel.fetchData() }) {
                                    Text("Spróbuj ponownie")
                                }
                            }
                        }
                    }
                    is UserDetailViewModel.UIState.Success -> {
                        val todos = (uiState as UserDetailViewModel.UIState.Success).todos
                        items(todos) { todo ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = todo.title,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp)
                                )
                                Checkbox(
                                    checked = todo.completed,
                                    onCheckedChange = null,
                                    enabled = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}