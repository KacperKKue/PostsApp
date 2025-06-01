package com.kacperkk.postsapp.ui.screens.profilescreen

import android.Manifest
import android.R.attr.name
import android.content.Context
import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute.Location
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.kacperkk.postsapp.model.User
import androidx.compose.runtime.getValue
import com.google.android.gms.maps.model.LatLng
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.Circle
import com.kacperkk.postsapp.data.utils.ImageUtils
import com.kacperkk.postsapp.data.utils.ImageUtils.getFileFromInternalStorage
import com.kacperkk.postsapp.ui.screens.postlist.PostListViewModel
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.internal.wait
import java.io.File
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel,
    postListViewModel: PostListViewModel,
    locationViewModel: LocationViewModel = viewModel(factory = LocationViewModel.Factory)
) {
    val context = LocalContext.current

    val userName by viewModel.userNameFlow.collectAsState(initial = "")
    val userSurname by viewModel.userSurnameFlow.collectAsState(initial = "")
    val avatarPath by viewModel.avatarPathFlow.collectAsState(initial = "")

    val users = remember { postListViewModel.getAllUsers() }

    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val uiState by locationViewModel.uiState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val savedFileName: String = ImageUtils.saveImageToInternalStorage(context, selectedUri)
            viewModel.updateAvatarPath(savedFileName)
        }
    }

    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            locationViewModel.fetchLocation()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") }
            ) {
                if (avatarPath.isNotBlank()) {
                    val imageFile: File = getFileFromInternalStorage(context, avatarPath)

                    if (imageFile.exists()) {
                        AsyncImage(
                            model = imageFile,
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Default Avatar",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Default Avatar",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = userName,
                onValueChange = { viewModel.updateUserName(it) },
                label = { Text("Imię") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = userSurname,
                onValueChange = { viewModel.updateUserSurname(it) },
                label = { Text("Nazwisko") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!permissionState.status.isGranted) {
                        Button(onClick = { permissionState.launchPermissionRequest() }) {
                            Text("Poproś o dostęp do lokalizacji")
                        }
                    } else {
                        when (uiState) {
                            is LocationViewModel.UIState.Success -> {
                                val location =
                                    (uiState as LocationViewModel.UIState.Success).location
                                Text("Twoja lokalizacja: ${location.latitude}, ${location.longitude}")
                                Spacer(modifier = Modifier.height(6.dp))

                                val cameraPositionState = rememberCameraPositionState {
                                    position = CameraPosition.fromLatLngZoom(
                                        LatLng(location.latitude, location.longitude), 10f
                                    )
                                }

                                val userLatLngs = remember(users) {
                                    users.mapNotNull { user ->
                                        val latDouble = user.address.geo.lat.toDoubleOrNull()
                                        val lngDouble = user.address.geo.lng.toDoubleOrNull()
                                        if (latDouble != null && lngDouble != null) {
                                            LatLng(latDouble, lngDouble)
                                        } else {
                                            null
                                        }
                                    }
                                }

                                GoogleMap(
                                    modifier = Modifier.fillMaxSize(),
                                    cameraPositionState = cameraPositionState
                                ) {
                                    Marker(
                                        state = MarkerState(
                                            position = LatLng(
                                                location.latitude,
                                                location.longitude
                                            )
                                        ),
                                        title = "Twoja lokalizacja",
                                    )
                                    userLatLngs.forEachIndexed { index, latLng ->
                                        Marker(
                                            state = MarkerState(position = latLng),
                                            title = "Użytkownik #${users[index].id}", // lub możesz użyć users[index].name
                                            snippet = users[index].name,
                                            icon = BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_AZURE
                                            )

                                        )
                                    }
                                }
                            }

                            LocationViewModel.UIState.Error -> {
                                Text("Nie udało się pobrać lokalizacji")
                            }

                            LocationViewModel.UIState.Loading -> {
                                Text("Pobieranie lokalizacji...")
                            }
                        }
                    }
                }
            }
        }
    }
}
