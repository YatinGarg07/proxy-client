package com.example.proxy.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proxy.MyApp
import com.example.proxy.models.ProxyDBItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    private val realm = MyApp.realm

    val proxyFromDB = realm
        .query<ProxyDBItem>()
        .asFlow()
        .map{ results->
            results.list.toList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    fun addProxyInDB(proxy: ProxyDBItem): ValidationResult{
        val validationResult = validateProxy(
            profileName = proxy.profileName,
            protocol = proxy.protocol,
            server = proxy.server,
            port = proxy.port,
            authMethod = proxy.authMethod,
            username = proxy.username,
            password = proxy.password
        )

        return if (validationResult is ValidationResult.Success) {
            viewModelScope.launch {
                realm.write {
                    copyToRealm(proxy, updatePolicy = UpdatePolicy.ALL)
                }
            }

            validationResult
        }
        else{
            validationResult
        }

    }



    fun validateProxy(
        profileName: String,
        protocol: String,
        server: String,
        port: String,
        authMethod: String,
        username: String?,
        password: String?
    ): ValidationResult {
        if (profileName.isBlank()) {
            return ValidationResult.Error("Profile name cannot be empty.")
        }
        if (protocol.isBlank()) {
            return ValidationResult.Error("Protocol cannot be empty.")
        }
        if (server.isBlank()) {
            return ValidationResult.Error("Server cannot be empty.")
        }
        if (port.isBlank() || !port.all { it.isDigit() }) {
            return ValidationResult.Error("Port must be a number.")
        }

        if (authMethod != "None" && username.isNullOrBlank()) {
            return ValidationResult.Error("Username cannot be empty.")
        }
        if (authMethod != "None" && password.isNullOrBlank()) {
            return ValidationResult.Error("Password cannot be empty.")
        }

        return ValidationResult.Success
    }

}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}