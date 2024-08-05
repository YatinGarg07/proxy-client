package com.example.proxy.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proxy.models.ProxyDBItem
import com.example.proxy.models.ProxyItem
import com.example.proxy.ui.theme.appBarColor
import com.example.proxy.ui.theme.selectedNavColor
import com.example.proxy.ui.theme.selectedNavIconColor

@Composable
fun AddProxyScreen(
    navController: NavController,
    viewModel: MainViewModel,
    isEditMode: Boolean,
    proxyItem: ProxyItem? = null
) {
    var isEditModeOverlayVisible by remember { mutableStateOf(isEditMode) }

    var profileName by remember { mutableStateOf("") }
    var protocol by remember { mutableStateOf("SOCKS5") }
    var server by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("") }
    var authMethod by remember { mutableStateOf("Username or Password") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isAuthExpanded by remember { mutableStateOf(false) }
    var isProtocolExpanded by remember { mutableStateOf(false) }

    val currentProxyItem by remember { mutableStateOf(ProxyDBItem()) }

    //this means you are coming from Add Proxy Button
    if(proxyItem != null){
        currentProxyItem.apply {
            this._id = proxyItem._id
        }
        profileName = proxyItem.profileName
        protocol = proxyItem.protocol
        server = proxyItem.server
        port = proxyItem.port
        authMethod = proxyItem.authMethod
        proxyItem.username?.let{
            username = it
        }
        proxyItem.password?.let{
            password = it
        }

    }

    val context = LocalContext.current
    Scaffold(
        topBar = {
            AddProxyAppBar(

                onBackClicked = {
                    navController.popBackStack()
                },
                onSaveClicked = {
                    if(isEditModeOverlayVisible){
                        isEditModeOverlayVisible = false
                    }
                    else if(!isEditModeOverlayVisible){

                        //save button
                        val validationResult = viewModel.addProxyInDB(

                            currentProxyItem.apply {
                                this.profileName = profileName
                                this.protocol = protocol
                                this.server = server
                                this.port = port
                                this.authMethod = authMethod
                                this.username = username
                                this.password = password
                            }

                        )

                        if(validationResult is ValidationResult.Error){
                            Toast.makeText(context,validationResult.message,Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(context,"New Profile Created",Toast.LENGTH_LONG).show()
                            isEditModeOverlayVisible = true
                        }
                    }



                },
                isEditModeOverlayVisible
            )
        }
    ) { innerPadding ->

        FormPage(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel,
            profileName,
            onProfileNameChange = { profileName = it },
            protocol,
            onProtocolChange = { protocol = it },
            isProtocolExpanded,
            onIsProtocolExpandedChange = { isProtocolExpanded = it },
            server,
            onServerChange = { server = it },
            port,
            onPortChange = { port = it },
            authMethod,
            onAuthMethodChange = { authMethod = it },
            username,
            onUsernameChange = { username = it },
            password,
            onPasswordChange = { password = it },
            passwordVisible,
            onPasswordVisibleChange = { passwordVisible = it },
            isAuthExpanded,
            onIsAuthExpandedChange = { isAuthExpanded = it }
        )

        if(isEditModeOverlayVisible){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .pointerInput(Unit){}
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPage(
    modifier: Modifier,
    viewModel: MainViewModel,
    profileName: String,
    onProfileNameChange: (String) -> Unit,
    protocol: String,
    onProtocolChange: (String) -> Unit,
    isProtocolExpanded: Boolean,
    onIsProtocolExpandedChange: (Boolean) -> Unit,
    server: String,
    onServerChange: (String) -> Unit,
    port: String,
    onPortChange: (String) -> Unit,
    authMethod: String,
    onAuthMethodChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: (Boolean) -> Unit,
    isAuthExpanded: Boolean,
    onIsAuthExpandedChange: (Boolean) -> Unit
) {


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA)) // Background color
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = profileName,
            onValueChange = onProfileNameChange,
            label = { Text("Profile name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = selectedNavIconColor,
                focusedIndicatorColor = selectedNavIconColor,
                unfocusedIndicatorColor = selectedNavColor,
                unfocusedLabelColor = Color.Black,
                focusedLabelColor = selectedNavIconColor,
            ).copy()
        )

        // Dropdown for Protocol
        ExposedDropdownMenuBox(
            expanded = isProtocolExpanded,
            onExpandedChange = onIsProtocolExpandedChange
        ) {
            OutlinedTextField(
                value = protocol,
                onValueChange = onProtocolChange,
                label = { Text("Protocol") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    cursorColor = selectedNavIconColor,
                    focusedIndicatorColor = selectedNavIconColor,
                    unfocusedIndicatorColor = selectedNavColor,
                    unfocusedLabelColor = Color.Black,
                    focusedLabelColor = selectedNavIconColor,
                ).copy()
            )
            ExposedDropdownMenu(
                expanded = isProtocolExpanded,
                onDismissRequest = { onIsProtocolExpandedChange(false) },
            ) {
                DropdownMenuItem(
                    text = { Text("SOCKS5") },
                    onClick = {
                        onProtocolChange("SOCKS5")
                        onIsProtocolExpandedChange(false)
                    }
                )
                DropdownMenuItem(
                    text = { Text("HTTP") },
                    onClick = {
                        onProtocolChange("HTTP")
                        onIsProtocolExpandedChange(false)
                    }
                )
            }
        }

        OutlinedTextField(
            value = server,
            onValueChange = onServerChange,
            label = { Text("Server") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = selectedNavIconColor,
                focusedIndicatorColor = selectedNavIconColor,
                unfocusedIndicatorColor = selectedNavColor,
                unfocusedLabelColor = Color.Black,
                focusedLabelColor = selectedNavIconColor,
            ).copy()
        )

        OutlinedTextField(
            value = port,
            onValueChange = onPortChange,
            label = { Text("Port") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = selectedNavIconColor,
                focusedIndicatorColor = selectedNavIconColor,
                unfocusedIndicatorColor = selectedNavColor,
                unfocusedLabelColor = Color.Black,
                focusedLabelColor = selectedNavIconColor,
            ).copy()
        )

        // Dropdown for Authentication method
        ExposedDropdownMenuBox(
            expanded = isAuthExpanded,
            onExpandedChange = onIsAuthExpandedChange
        ) {
            OutlinedTextField(
                value = authMethod,
                onValueChange = { onAuthMethodChange(it) },
                label = { Text("Authentication method") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    cursorColor = selectedNavIconColor,
                    focusedIndicatorColor = selectedNavIconColor,
                    unfocusedIndicatorColor = selectedNavColor,
                    unfocusedLabelColor = Color.Black,
                    focusedLabelColor = selectedNavIconColor,
                ).copy()
            )
            ExposedDropdownMenu(
                expanded = isAuthExpanded,
                onDismissRequest = { onIsAuthExpandedChange(false) },
                modifier = Modifier.fillMaxWidth()
            ) {
                DropdownMenuItem(
                    text = { Text("Username or Password") },
                    onClick = {
                        onAuthMethodChange("Username or Password")
                        onIsAuthExpandedChange(false)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenuItem(
                    text = { Text("None") },
                    onClick = {
                        onAuthMethodChange("None")
                        onIsAuthExpandedChange(false)
                    },

                    modifier = Modifier.fillMaxWidth()
                )
            }


        }
        if (authMethod == "Username or Password") {
            AuthDetails(
                username = username,
                onUsernameChange = onUsernameChange,
                password = password,
                onPasswordChange = onPasswordChange,
                passwordVisible = passwordVisible,
                onPasswordVisibleChange = { onPasswordVisibleChange(!passwordVisible) }
            )
        }


    }
}

@Composable
fun AuthDetails(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleChange: () -> Unit
) {
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChange,
        label = { Text("Username") },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            cursorColor = selectedNavIconColor,
            focusedIndicatorColor = selectedNavIconColor,
            unfocusedIndicatorColor = selectedNavColor,
            unfocusedLabelColor = Color.Black,
            focusedLabelColor = selectedNavIconColor,
        ).copy(),

        )

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            IconButton(onClick = onPasswordVisibleChange) {
                Icon(imageVector = image, contentDescription = null)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            cursorColor = selectedNavIconColor,
            focusedIndicatorColor = selectedNavIconColor,
            unfocusedIndicatorColor = selectedNavColor,
            unfocusedLabelColor = Color.Black,
            focusedLabelColor = selectedNavIconColor,
        ).copy()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProxyAppBar(
    onBackClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    isEditMode: Boolean
) {
    TopAppBar(
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = appBarColor
        ).copy(),

        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "go back"
                )
            }
        },
        actions = {
            IconButton(onClick = onSaveClicked)
            {
                Icon(
                    imageVector = if(isEditMode) Icons.Filled.Edit else Icons.Filled.Save,
                    contentDescription = "save"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun FormPagePreview() {
//        FormPage()
}