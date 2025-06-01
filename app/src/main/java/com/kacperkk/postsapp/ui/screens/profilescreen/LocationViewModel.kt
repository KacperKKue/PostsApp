package com.kacperkk.postsapp.ui.screens.profilescreen

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    sealed interface UIState {
        data class Success(val location: LatLng) : UIState
        object Error : UIState
        object Loading : UIState
    }

    fun fetchLocation() {
        val context = getApplication<Application>()
        val client = fusedLocationClient

        try {
            client.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    _uiState.value = UIState.Success(LatLng(location.latitude, location.longitude))
                } else {
                    _uiState.value = UIState.Error
                }
            }.addOnFailureListener {
                _uiState.value = UIState.Error
            }
        } catch (e: SecurityException) {
            _uiState.value = UIState.Error
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                LocationViewModel(app)
            }
        }
    }
}
