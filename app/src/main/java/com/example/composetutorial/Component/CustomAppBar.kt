package com.example.composetutorial.Component

import android.graphics.drawable.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.composetutorial.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(modifier: Modifier = Modifier,
                 title: String,
                 backgroundColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                 navigationIcon: ImageVector = Icons.Filled.ArrowBack,
                 elementColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                 onNavigationIconClick: ()->Unit) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                modifier = Modifier,
                onClick = onNavigationIconClick,
            ) {
                Icon(navigationIcon, "", tint = elementColor)
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = backgroundColor),
        title = {
            Text( modifier = modifier,
                text = title,
                color = elementColor,
                style = MaterialTheme.typography.h6)
        })
}

@Preview
@Composable
fun Preview() {
    //CustomAppBar(title = "Welcome", )
}