package com.example.composetutorial.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

class MobileLoginScreen {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileLogin(modifier: Modifier = Modifier){
    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var countryCode by remember { mutableStateOf(TextFieldValue("+91")) }
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var showPassword by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phone,
            leadingIcon = {
                //Icon(imageVector = Icons.Default.Phone, contentDescription = "phoneIcon")
                OutlinedTextField(modifier = Modifier.width(104.dp),
                    value = countryCode,
                    leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = "phoneIcon") },
                    onValueChange = {
                        countryCode = it
                    })
            },
            //trailingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = "phoneIcon") },
            onValueChange = {
                phone = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(text = "Phone number") },
            placeholder = { Text(text = "Enter your phone number") },
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "passwordIcon") },
            trailingIcon = { if (showPassword){
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = "visibilityIcon")
                }
            } else {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(imageVector = Icons.Default.VisibilityOff, contentDescription = "visibilityOffIcon",)
                }
            }
            },
            onValueChange = {
                text = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = { Text(text = "Password") },
            placeholder = { Text(text = "Enter your password") },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
        )

    }

}