package com.example.vin.petclinicappointment.ui.components.sign_up

import android.util.Patterns
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.data.model.Customer
import com.example.vin.petclinicappointment.ui.components.common.AppButton
import com.example.vin.petclinicappointment.ui.components.common.CircularProgressIndicator
import com.example.vin.petclinicappointment.ui.components.common.TextInput
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@Composable
fun CustomerSignUpPage(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit
){
    var name by rememberSaveable{ mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showNameError by rememberSaveable { mutableStateOf(false)}
    var nameErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var showEmailError by rememberSaveable { mutableStateOf( false )}
    var emailErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var showPasswordError by rememberSaveable { mutableStateOf( false )}
    var passwordErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }

    val localFocusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        registerViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    fun onClickSignUp(){
        nameErrorMessage = validateUserName(email)
        if(!nameErrorMessage.isNullOrEmpty()){
            showNameError = true
            return
        }
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
            registerViewModel.register(Customer(email = email, password = password, name = name))
            progressIndicatorVisible = false
            if (registerViewModel.isLoggedIn.value) {
                navigateToHome()
            }
        }
    }

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
        Column (
            Modifier
                .fillMaxSize()
                .padding(
                    top = PetClinicAppointmentTheme.dimensions.grid_7
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
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
                    .height(PetClinicAppointmentTheme.dimensions.grid_5*10),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "Daftar",
                    style = PetClinicAppointmentTheme.typography.h1,
                    modifier = Modifier.padding(
                        top = PetClinicAppointmentTheme.dimensions.grid_4,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_8
                    )
                )
                TextInput(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Nama Lengkap",
                    containerModifier = Modifier
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2),
                    showError = showNameError,
                    errorMessage = nameErrorMessage,
                    onFocus = { showNameError = false },
                    onLeaveFocus = {
                        nameErrorMessage = validateUserName(name)
                        if (!nameErrorMessage.isNullOrEmpty()) {
                            showNameError = true
                        }
                    }
                )
                TextInput(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    containerModifier = Modifier
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2),
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
                    onValueChange = { password = it },
                    placeholder = "Kata Sandi",
                    containerModifier = Modifier
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_4),
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
                    onClick = { onClickSignUp() },
                    modifier = Modifier
                        .width(PetClinicAppointmentTheme.dimensions.grid_9 * 4)
                        .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                    colors = buttonColors(PetClinicAppointmentTheme.colors.secondary),
                    shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                ) {
                    Text(
                        text = "Daftar",
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
            ) {
                Row {
                    Text("Sudah punya akun?")
                    Spacer(modifier = Modifier.width(PetClinicAppointmentTheme.dimensions.grid_1))
                    ClickableText(
                        text = AnnotatedString("Masuk"),
                        onClick = { navigateToLogin() },
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

fun validateUserName(username: String): String? {
    if(username.isEmpty()){
        return "Nama wajib diinput."
    }
    return null
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
        return "Kata sandi harus memiliki minimal 8 karakter yang terdiri huruf besar, huruf kecil, dan angka."
    }
    return null
}