package com.example.vin.petclinicappointment.ui.components.sign_up

import android.util.Patterns
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vin.petclinicappointment.ui.components.common.AppButton
import com.example.vin.petclinicappointment.ui.components.common.TextInput
import com.example.vin.petclinicappointment.ui.theme.DarkTurqoise
import com.example.vin.petclinicappointment.ui.utils.Metric
import com.example.vin.petclinicappointment.ui.utils.SetSizeByWindowType
import com.example.vin.petclinicappointment.ui.utils.WindowType
import com.example.vin.petclinicappointment.ui.utils.rememberWindowInfo
import java.util.regex.Pattern

@Composable
fun SignUpPage(
    navigateToLogin: () -> Unit
){
    var name by rememberSaveable{ mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showNameError by rememberSaveable { mutableStateOf(false)}
    var nameErrorMessage by rememberSaveable { mutableStateOf( "" )}
    var showEmailError by rememberSaveable { mutableStateOf( false )}
    var emailErrorMessage by rememberSaveable { mutableStateOf( "" )}
    var showPasswordError by rememberSaveable { mutableStateOf( false )}
    var passwordErrorMessage by rememberSaveable { mutableStateOf( "" )}
    val windowInfo = rememberWindowInfo()
    val localFocusManager = LocalFocusManager.current

    Surface(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
            detectTapGestures(onTap = {
                localFocusManager.clearFocus()
            })
        },
        color = MaterialTheme.colors.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Spacer(modifier = Modifier.height(SetSizeByWindowType(Metric.Height, 50.dp, 50.dp, 150.dp)))
            Text(
                text = "PetClinic",
                style = MaterialTheme.typography.h1
            )
            Spacer(modifier = Modifier.height(SetSizeByWindowType(Metric.Height, 70.dp, 70.dp, 180.dp)))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextInput(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Nama Lengkap",
                    containerModifier = Modifier
                        .fillMaxWidth(
                            if (windowInfo.width.type == WindowType.Compact) 0.7f else 0.5f
                        )
                        .padding(bottom = 20.dp),
                    showError = showNameError,
                    errorMessage = nameErrorMessage,
                    onFocus = { showNameError = false },
                    onLeaveFocus = {
                        val errorMessage = validateUserName(email)
                        if (errorMessage.isNotEmpty()) {
                            showNameError = true
                            nameErrorMessage = errorMessage
                        }
                    }
                )
                TextInput(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    containerModifier = Modifier
                        .fillMaxWidth(
                            if (windowInfo.width.type == WindowType.Compact) 0.7f else 0.5f
                        )
                        .padding(bottom = 20.dp),
                    showError = showEmailError,
                    errorMessage = emailErrorMessage,
                    onFocus = { showEmailError = false },
                    onLeaveFocus = {
                        val errorMessage = validateEmail(email)
                        if (errorMessage.isNotEmpty()) {
                            showEmailError = true
                            emailErrorMessage = errorMessage
                        }
                    }
                )
                TextInput(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    containerModifier = Modifier
                        .fillMaxWidth(
                            if (windowInfo.width.type == WindowType.Compact) 0.7f else 0.5f
                        )
                        .padding(bottom = 40.dp),
                    showError = showPasswordError,
                    errorMessage = passwordErrorMessage,
                    onFocus = { showPasswordError = false },
                    onLeaveFocus = {
                        val errorMessage = validatePassword(email)
                        if (errorMessage.isNotEmpty()) {
                            showPasswordError = true
                            passwordErrorMessage = errorMessage
                        }
                    }
                )
                AppButton(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth(
                            if (windowInfo.width.type == WindowType.Compact) 0.7f else 0.5f
                        )
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(DarkTurqoise),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Daftar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(60.dp))
            Row {
                Text("Sudah punya akun?")
                Spacer(modifier = Modifier.width(8.dp))
                ClickableText(
                    text = AnnotatedString("Masuk"),
                    onClick = { navigateToLogin() },
                    style = TextStyle(color = DarkTurqoise)
                )
            }
        }
    }
}

fun validateUserName(username: String): String {
    if(username.isEmpty()){
        return "Name is a required field"
    }
    return ""
}

fun validateEmail(email: String): String {
    if(email.isEmpty()){
        return "Email is a required field"
    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return "Email format is invalid"
    }
    return ""
}

fun validatePassword(password: String): String {
    val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$"
    val passwordPattern = Pattern.compile(passwordRegex)
    if(password.isEmpty()){
        return "Password is a required field"
    }
    if(!passwordPattern.matcher(password).matches()) {
        return "Password must contains at least 8 characters that includes an uppercase letter, a lowercase letter, and a number"
    }
    return ""
}