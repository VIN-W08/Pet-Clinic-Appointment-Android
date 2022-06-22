package com.example.vin.petclinicappointment.ui.components.login

import android.util.Patterns
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.ui.components.common.AppButton
import com.example.vin.petclinicappointment.ui.components.common.TextInput
import com.example.vin.petclinicappointment.ui.theme.*
import kotlinx.coroutines.*
import java.util.regex.Pattern
import com.example.vin.petclinicappointment.ui.components.common.CircularProgressIndicator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginPage(
    loginViewModel: LoginViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: ScaffoldState,
    navigateToCustomerHome: () -> Unit,
    navigateToAppointment: () -> Unit,
    navigateToSignUp: () -> Unit,
    userRole: MutableState<String?>,
){
    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()
    var showEmailError by rememberSaveable { mutableStateOf( false )}
    var emailErrorMessage by rememberSaveable { mutableStateOf<String?>( null)}
    var showPasswordError by rememberSaveable { mutableStateOf( false )}
    var passwordErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }

    val localFocusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        loginViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    fun onClickLogin(){
        emailErrorMessage = validateEmail(email)
        if(!emailErrorMessage.isNullOrEmpty()){
            showEmailError = true
            return
        }
        passwordErrorMessage = validatePassword(password)
        if(!passwordErrorMessage.isNullOrEmpty()){
            showPasswordError = true
            return
        }
        progressIndicatorVisible = true
        coroutineScope.launch {
            when(userRole.value) {
                "customer" -> loginViewModel.login()
                "clinic" -> loginViewModel.loginClinic()
                else -> return@launch
            }
            progressIndicatorVisible = false
            if (loginViewModel.isLoggedIn.value) {
                if(userRole.value.equals("customer")) {
                    navigateToCustomerHome()
                }else if(userRole.value.equals("clinic")){
                    navigateToAppointment()
                }
            }
        }
    }

    fun onClickSignUp() = navigateToSignUp()

    Surface(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    localFocusManager.clearFocus()
                })
            }
            .verticalScroll(rememberScrollState()),
        color = MaterialTheme.colors.background
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(
                    top = PetClinicAppointmentTheme.dimensions.grid_7
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(PetClinicAppointmentTheme.dimensions.grid_10),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "PetClinic",
                    style = MaterialTheme.typography.h1
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PetClinicAppointmentTheme.dimensions.grid_5 * 9),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "Masuk",
                    style = PetClinicAppointmentTheme.typography.h1,
                    modifier = Modifier.padding(
                        top = PetClinicAppointmentTheme.dimensions.grid_4,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_8
                    )
                )
                TextInput(
                    value = email,
                    onValueChange = { loginViewModel.setEmail(it) },
                    placeholder = "Email",
                    containerModifier = Modifier
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2),
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    showError = showEmailError,
                    errorMessage = emailErrorMessage,
                    onFocus = { showEmailError = false },
                    onLeaveFocus = {
                        emailErrorMessage = validateEmail(email)
                        if (!emailErrorMessage.isNullOrEmpty()) {
                            showEmailError = true
                        }
                    }
                )
                TextInput(
                    value = password,
                    onValueChange = { loginViewModel.setPassword(it) },
                    placeholder = "Kata Sandi",
                    containerModifier = Modifier
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_4_5),
                    keyBoardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        autoCorrect = false
                    ),
                    inputType = "password",
                    showError = showPasswordError,
                    errorMessage = passwordErrorMessage,
                    onFocus = { showPasswordError = false },
                    onLeaveFocus = {
                        passwordErrorMessage = validatePassword(password)
                        if (!passwordErrorMessage.isNullOrEmpty()) {
                            showPasswordError = true
                        }
                    }
                )
                AppButton(
                    onClick = { onClickLogin() },
                    modifier = Modifier
                        .width(PetClinicAppointmentTheme.dimensions.grid_3 * 11)
                        .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                    colors = buttonColors(PetClinicAppointmentTheme.colors.primary),
                    shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                ) {
                    Text(
                        text = "Masuk",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column (
                Modifier
                    .fillMaxWidth()
                    .padding(
                        top = PetClinicAppointmentTheme.dimensions.grid_2,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_2
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
                    ){
                ClickableText(
                    text = AnnotatedString("Lupa Password?"),
                    onClick = {},
                    style = TextStyle(color = PetClinicAppointmentTheme.colors.secondary)
                )
                Spacer(Modifier.height(PetClinicAppointmentTheme.dimensions.grid_4))
                Row {
                    Text("Belum punya akun?")
                    Spacer(modifier = Modifier.width(PetClinicAppointmentTheme.dimensions.grid_1))
                    ClickableText(
                        text = AnnotatedString("Daftar Akun"),
                        onClick = { onClickSignUp() },
                        style = TextStyle(color = PetClinicAppointmentTheme.colors.secondary)
                    )
                }
            }
        }
    }
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(visible = progressIndicatorVisible)
    }
}

fun validateEmail(email: String): String? {
    if(email.isEmpty()){
        return "Email wajib diinput."
    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return "Format email salah."
    }
    return null
}

fun validatePassword(password: String): String? {
    val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$"
    val passwordPattern = Pattern.compile(passwordRegex)
    if(password.isEmpty()){
        return "Kata sandi wajib diinput."
    }
    if(!passwordPattern.matcher(password).matches()) {
        return "Kata sandi harus memiliki minimal 8 karakter yang terdiri dari huruf besar, huruf kecil, dan angka."
    }
    return null
}