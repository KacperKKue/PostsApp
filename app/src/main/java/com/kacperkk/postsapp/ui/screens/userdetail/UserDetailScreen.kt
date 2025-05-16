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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kacperkk.postsapp.model.User

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun UserDetailScreen(
    navController: NavController,
    user: User?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profil użytkownika",
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
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
            }
        }
    }
}
