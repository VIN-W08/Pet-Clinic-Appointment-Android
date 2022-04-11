package com.example.vin.petclinicappointment.ui.components.login

import android.util.Patterns
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.data.model.User
import com.example.vin.petclinicappointment.ui.components.common.AppButton
import com.example.vin.petclinicappointment.ui.components.common.TextInput
import com.example.vin.petclinicappointment.ui.theme.BrightNavyBlue
import com.example.vin.petclinicappointment.ui.theme.DarkTurqoise
import com.example.vin.petclinicappointment.ui.theme.TaupeGray
import com.example.vin.petclinicappointment.ui.theme.Water
import com.example.vin.petclinicappointment.ui.utils.Metric
import com.example.vin.petclinicappointment.ui.utils.SetSizeByWindowType
import com.example.vin.petclinicappointment.ui.utils.WindowType
import com.example.vin.petclinicappointment.ui.utils.rememberWindowInfo
import kotlinx.coroutines.*
import java.util.regex.Pattern

@Composable
fun LoginPage(
    loginViewModel: LoginViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit
){
    var email by rememberSaveable { mutableStateOf("")}
    var password by rememberSaveable { mutableStateOf("")}
    var showEmailError by rememberSaveable { mutableStateOf( false )}
    var emailErrorMessage by rememberSaveable { mutableStateOf( "" )}
    var showPasswordError by rememberSaveable { mutableStateOf( false )}
    var passwordErrorMessage by rememberSaveable { mutableStateOf( "" )}

    val localFocusManager = LocalFocusManager.current
    val windowInfo = rememberWindowInfo()

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
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(SetSizeByWindowType(Metric.Height, 50.dp, 50.dp, 150.dp)))
            Text(
                text = "PetClinic",
                style = MaterialTheme.typography.h1
            )
            Spacer(modifier = Modifier.height(SetSizeByWindowType(Metric.Height, 70.dp, 70.dp, 180.dp)))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppButton(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth(
                            if (windowInfo.width.type == WindowType.Compact) 0.7f else 0.5f
                        )
                        .height(55.dp),
                    colors = buttonColors(Water),
                    border = BorderStroke(2.dp, TaupeGray),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_logo),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Masuk Dengan Google",
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "atau",
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.height(20.dp))
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
                    inputType = "password",
                    showError = showPasswordError,
                    errorMessage = passwordErrorMessage,
                    onFocus = { showPasswordError = false },
                    onLeaveFocus = {
                        val errorMessage = validatePassword(password)
                        if (errorMessage.isNotEmpty()) {
                            showPasswordError = true
                            passwordErrorMessage = errorMessage
                        }
                    }
                )
                AppButton(
                    onClick = {
                        coroutineScope.launch {
                            loginViewModel.login(User(email, password))
                            if (loginViewModel.isLoggedIn.value) {
                                navigateToHome()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(
                            if (windowInfo.width.type == WindowType.Compact) 0.7f else 0.5f
                        )
                        .height(55.dp),
                    colors = buttonColors(BrightNavyBlue),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Masuk",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(60.dp))
            ClickableText(
                text = AnnotatedString("Lupa Password?"),
                onClick = {},
                style = TextStyle(color = DarkTurqoise)
            )
            Spacer(Modifier.height(30.dp))
            Row {
                Text("Belum punya akun?")
                Spacer(modifier = Modifier.width(8.dp))
                ClickableText(
                    text = AnnotatedString("Daftar Akun"),
                    onClick = { navigateToSignUp() },
                    style = TextStyle(color = DarkTurqoise)
                )
            }
        }
    }
}

fun validateEmail(email: String): String {
    if(email.isEmpty()){
        return "Email is a required field."
    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return "Email format is invalid."
    }
    return ""
}

fun validatePassword(password: String): String {
    val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$"
    val passwordPattern = Pattern.compile(passwordRegex)
    if(password.isEmpty()){
        return "Password is a required field."
    }
    if(!passwordPattern.matcher(password).matches()) {
        return "Password must contains at least 8 characters that includes an uppercase letter, a lowercase letter, and a number."
    }
    return ""
}