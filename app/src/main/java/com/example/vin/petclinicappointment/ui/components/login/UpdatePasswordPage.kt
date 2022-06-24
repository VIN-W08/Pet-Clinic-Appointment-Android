package com.example.vin.petclinicappointment.ui.components.login

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.ui.components.common.AppButton
import com.example.vin.petclinicappointment.ui.components.common.CircularProgressIndicator
import com.example.vin.petclinicappointment.ui.components.common.IconButton
import com.example.vin.petclinicappointment.ui.components.common.TextInput
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun UpdatePasswordPage(
    navigateBack: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    userRole: MutableState<String?>
){
    val localFocusManager = LocalFocusManager.current
    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()
    var showEmailError by rememberSaveable { mutableStateOf( false ) }
    var emailErrorMessage by rememberSaveable { mutableStateOf<String?>( null) }
    var showPasswordError by rememberSaveable { mutableStateOf( false ) }
    var passwordErrorMessage by rememberSaveable { mutableStateOf<String?>( null ) }
    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        loginViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    fun onClickChangePassword(){
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
        val userRole = userRole.value
        if(userRole !== null) {
            coroutineScope.launch {
                progressIndicatorVisible = true
                val isSuccess = loginViewModel.updatePassword(userRole)
                if (isSuccess) {
                    navigateBack()
                }
                progressIndicatorVisible = false
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
        color = PetClinicAppointmentTheme.colors.background
    ){
        Column(
            Modifier.fillMaxSize()
        ){
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                        end = PetClinicAppointmentTheme.dimensions.grid_2,
                        top = PetClinicAppointmentTheme.dimensions.grid_2,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_4
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.update_password),
                    style = PetClinicAppointmentTheme.typography.h1
                )
            }
            Column (
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
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
                    onClick = { onClickChangePassword() },
                    modifier = Modifier
                        .width(PetClinicAppointmentTheme.dimensions.grid_3 * 11)
                        .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                    colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.primary),
                    shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                ) {
                    Text(
                        text = stringResource(R.string.change),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(visible = progressIndicatorVisible)
        }
        Box {
            Row (
                Modifier.height(PetClinicAppointmentTheme.dimensions.grid_7_5),
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    icon = Icons.Rounded.ArrowBackIos,
                    contentDescription = "arrow_back",
                    onClick = { navigateBack() },
                    modifier = Modifier
                        .padding(start = PetClinicAppointmentTheme.dimensions.grid_2)
                        .size(PetClinicAppointmentTheme.dimensions.grid_2_5)
                )
            }
        }
    }
}