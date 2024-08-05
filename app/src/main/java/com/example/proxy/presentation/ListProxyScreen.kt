package com.example.proxy.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.proxy.EditProxyDest
import com.example.proxy.ui.theme.appBarColor
import com.example.proxy.ui.theme.selectedNavColor

@Composable
fun ListProxyScreen(viewModel: MainViewModel, navController: NavController){
    val proxies by viewModel.proxyFromDB.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AppBar() },
        containerColor = selectedNavColor,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            itemsIndexed(proxies){index, item ->
                ProxyItem(
                    title = item.profileName,
                    address = "${item.server}:${item.port}",
                    protocol = item.protocol,
                    onProxyItemClicked = {
                            navController.navigate(
                                EditProxyDest(
                                    isEditMode = true,
                                    proxyItem = proxies.get(index).toProxyItem()
                                )
                            )
                    }
                )
            }
        }
    }


}

@Composable
fun ProxyItem(
    title: String,
    address: String,
    protocol: String,
    onProxyItemClicked: ()->Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProxyItemClicked() }
            .background(Color(0xFFF7FBFC))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Star",
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(18.dp)
                )
            }
            Text(
                text = address,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Text(
            text = protocol,
            fontSize = 12.sp,
            color = Color.White,
            modifier = Modifier
                .background(Color(0xFF0078B7), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Arrow",
            tint = Color.Gray,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}





@Preview(showBackground = true)
@Composable
fun ProxyItemPreview() {
//
//        ProxyItem(
//            title = "Default Profile",
//            address = "13.201.191.138:8082",
//            protocol = "HTTP"
//        )
    }