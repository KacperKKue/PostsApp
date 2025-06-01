package com.kacperkk.postsapp.ui.screens.userdetail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kacperkk.postsapp.ui.components.MapItem
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    navController: NavController,
    user: User?
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = user?.name ?: "Profil",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Powrót")
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
                Text("Użytkownik nie znaleziony", style = MaterialTheme.typography.bodyLarge)
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(user.name, style = MaterialTheme.typography.headlineSmall)
                            Text(
                                "@${user.username}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            ContactItem("📧", user.email) {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:${user.email}")
                                }
                                context.startActivity(intent)
                            }
                            ContactItem("📞", user.phone) {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${user.phone}")
                                }
                                context.startActivity(intent)
                            }
                            ContactItem("🌐", user.website) {
                                val url = "https://${user.website}"
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = url.toUri()
                                }
                                context.startActivity(intent)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("🏠 Adres:", style = MaterialTheme.typography.titleMedium)
                            Text("${user.address.street}, ${user.address.suite}")
                            Text("${user.address.city}, ${user.address.zipcode}")

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("🏢 Firma:", style = MaterialTheme.typography.titleMedium)
                            Text(user.company.name, fontWeight = FontWeight.Bold)
                            Text(user.company.catchPhrase)
                            Text(user.company.bs)
                        }
                    }
                }

                item {
                    Text("🌐 Mapa:", style = MaterialTheme.typography.titleMedium)

                    Spacer(modifier = Modifier.height(6.dp))

                    MapItem(
                        lat = user.address.geo.lat.toDouble(),
                        lng = user.address.geo.lng.toDouble(),
                        street = user.address.street,
                        suite = user.address.suite,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    )
                }

                item {
                    Text("📝 Zadania:", style = MaterialTheme.typography.titleMedium)
                }

                when (uiState) {
                    is UserDetailViewModel.UIState.Loading -> {
                        item {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            }
                        }
                    }

                    is UserDetailViewModel.UIState.Error -> {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Błąd ładowania zadań.", color = MaterialTheme.colorScheme.error)
                                Button(onClick = { viewModel.fetchData() }) {
                                    Text("Spróbuj ponownie")
                                }
                            }
                        }
                    }

                    is UserDetailViewModel.UIState.Success -> {
                        val todos = (uiState as UserDetailViewModel.UIState.Success).todos
                        items(todos) { todo ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (todo.completed) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = todo.title,
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyMedium
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
}

@Composable
fun ContactItem(icon: String, value: String, onClick: () -> Unit) {
    Text(
        text = "$icon $value",
        modifier = Modifier
            .padding(vertical = 2.dp)
            .clickable { onClick() },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary
    )
}
