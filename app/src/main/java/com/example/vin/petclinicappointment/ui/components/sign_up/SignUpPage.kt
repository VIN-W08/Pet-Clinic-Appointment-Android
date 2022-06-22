package com.example.vin.petclinicappointment.ui.components.sign_up

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vin.petclinicappointment.data.model.Customer
import com.example.vin.petclinicappointment.ui.components.common.*
import com.example.vin.petclinicappointment.ui.components.profile.EditClinicProfileViewModel
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@Composable
fun SignUpPage(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
    userRole: String
){
    LaunchedEffect(Unit) {
        registerViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    if(userRole == "customer"){
        CustomerSignUpContent(
            registerViewModel = registerViewModel,
            navigateToLogin = navigateToLogin,
            navigateToHome = navigateToHome
        )
    }else if(userRole == "clinic"){
        ClinicSignUpContent(
            registerViewModel = registerViewModel,
            navigateToLogin = navigateToLogin,
            navigateToHome = navigateToHome,
        )
    }
}

@Composable
fun CustomerSignUpContent(
    registerViewModel: RegisterViewModel,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit
){
    val name by registerViewModel.name.collectAsState()
    val email by registerViewModel.email.collectAsState()
    val password by registerViewModel.password.collectAsState()

    var showNameError by rememberSaveable { mutableStateOf(false)}
    var showEmailError by rememberSaveable { mutableStateOf( false )}
    var showPasswordError by rememberSaveable { mutableStateOf( false )}

    var nameErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var emailErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var passwordErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}

    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val localFocusManager = LocalFocusManager.current

    fun onClickSignUp(){
        nameErrorMessage = validateUserName(name)
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
            registerViewModel.registerCustomer(Customer(email = email, password = password, name = name))
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
                    .height(PetClinicAppointmentTheme.dimensions.grid_5 * 10),
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
                    onValueChange = { registerViewModel.setName(it) },
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
                    onValueChange = { registerViewModel.setEmail(it) },
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
                    onValueChange = { registerViewModel.setPassword(it) },
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
                        .width(PetClinicAppointmentTheme.dimensions.grid_3 * 11)
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
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(visible = progressIndicatorVisible)
        }
    }
}

@Composable
fun ClinicSignUpContent(
    registerViewModel: RegisterViewModel,
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit
){
    val name by registerViewModel.name.collectAsState()
    val email by registerViewModel.email.collectAsState()
    val password by registerViewModel.password.collectAsState()
    val phoneNum by registerViewModel.phoneNum.collectAsState()
    val address by registerViewModel.address.collectAsState()
    val latitude by registerViewModel.latitude.collectAsState()
    val longitude by registerViewModel.longitude.collectAsState()

    var showNameError by rememberSaveable { mutableStateOf(false)}
    var showEmailError by rememberSaveable { mutableStateOf( false )}
    var showPasswordError by rememberSaveable { mutableStateOf( false )}
    var showPhoneNumError by rememberSaveable { mutableStateOf(false)}
    var showAddressError by rememberSaveable { mutableStateOf(false)}
    var showLatError by rememberSaveable { mutableStateOf(false)}
    var showLonError by rememberSaveable { mutableStateOf(false)}

    var nameErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var emailErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var passwordErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var phoneNumErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var addressErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var latErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var lonErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}

    val coroutineScope = rememberCoroutineScope()
    val localFocusManager = LocalFocusManager.current

    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }

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
        phoneNumErrorMessage = validatePhoneNum(phoneNum)
        if(!phoneNumErrorMessage.isNullOrEmpty()){
            showPhoneNumError = true
            return
        }
        addressErrorMessage = validateAddress(address)
        if(!addressErrorMessage.isNullOrEmpty()){
            showAddressError = true
            return
        }
        latErrorMessage = validateLatitude(latitude)
        if(!latErrorMessage.isNullOrEmpty()){
            showLatError = true
            return
        }
        lonErrorMessage = validateLongitude(longitude)
        if(!lonErrorMessage.isNullOrEmpty()){
            showLonError = true
            return
        }
        progressIndicatorVisible = true
        coroutineScope.launch {
            registerViewModel.registerClinic()
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
            },
        color = MaterialTheme.colors.background
    ) {
        Column (
            Modifier
                .fillMaxSize()
                .padding(
                    top = PetClinicAppointmentTheme.dimensions.grid_2
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column (
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                        top = PetClinicAppointmentTheme.dimensions.grid_2,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_2,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    "Daftar Klinik Hewan",
                    style = PetClinicAppointmentTheme.typography.h1,
                )
            }
            Column (
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ){
                Text(
                    "Data Pribadi Klinik",
                    style = PetClinicAppointmentTheme.typography.h3,
                    modifier = Modifier.padding(
                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                        top = PetClinicAppointmentTheme.dimensions.grid_2,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_0_5
                    )
                )
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2)
                )
                TextInput(
                    value = name,
                    onValueChange = { registerViewModel.setName(it) },
                    placeholder = "Nama Lengkap",
                    containerModifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2
                        )
                        .fillMaxWidth(),
                    modifier = Modifier.fillMaxWidth(),
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
                    onValueChange = { registerViewModel.setEmail(it) },
                    placeholder = "Email",
                    containerModifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2
                        )
                        .fillMaxWidth(),
                    modifier = Modifier.fillMaxWidth(),
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
                    onValueChange = { registerViewModel.setPassword(it) },
                    placeholder = "Kata Sandi",
                    containerModifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2,
                        )
                        .fillMaxWidth(),
                    modifier = Modifier.fillMaxWidth(),
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
                TextInput(
                    value = phoneNum,
                    onValueChange = { registerViewModel.setPhoneNum(it) },
                    placeholder = "No. Telp",
                    containerModifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_4
                        )
                        .fillMaxWidth(),
                    modifier = Modifier.fillMaxWidth(),
                    showError = showPhoneNumError,
                    errorMessage = phoneNumErrorMessage,
                    onFocus = { showPhoneNumError = false },
                    onLeaveFocus = {
                        phoneNumErrorMessage = validatePhoneNum(phoneNum)
                        if (!phoneNumErrorMessage.isNullOrEmpty()) {
                            showPhoneNumError = true
                        }
                    }
                )
                Text(
                    "Data Lokasi Klinik",
                    style = PetClinicAppointmentTheme.typography.h3,
                    modifier = Modifier.padding(
                        start = PetClinicAppointmentTheme.dimensions.grid_2,
                        bottom = PetClinicAppointmentTheme.dimensions.grid_0_5
                    )
                )
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2)
                )
                TextInput(
                    value = address,
                    onValueChange = { registerViewModel.setAddress(it) },
                    placeholder = "Alamat",
                    containerModifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2
                        )
                        .fillMaxWidth(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PetClinicAppointmentTheme.dimensions.grid_10),
                    singleLine = false,
                    maxLines = 5,
                    showError = showAddressError,
                    errorMessage = addressErrorMessage,
                    onFocus = { showAddressError = false },
                    onLeaveFocus = {
                        addressErrorMessage = validateAddress(address)
                        if (!addressErrorMessage.isNullOrEmpty()) {
                            showAddressError = true
                        }
                    }
                )
                LocationInputs(
                    registerViewModel = registerViewModel,
                    containerModifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2
                        )
                        .fillMaxWidth()
                )
                TextInput(
                    value = latitude,
                    onValueChange = { registerViewModel.setLatitude(it) },
                    placeholder = "Lintang",
                    containerModifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_2
                        )
                        .fillMaxWidth(),
                    modifier = Modifier.fillMaxWidth(),
                    showError = showLatError,
                    errorMessage = latErrorMessage,
                    onFocus = { showLatError = false },
                    onLeaveFocus = {
                        latErrorMessage = validateLatitude(latitude)
                        if (!latErrorMessage.isNullOrEmpty()) {
                            showLatError = true
                        }
                    }
                )
                TextInput(
                    value = longitude,
                    onValueChange = { registerViewModel.setLongitude(it) },
                    placeholder = "Bujur",
                    containerModifier = Modifier
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_4
                        )
                        .fillMaxWidth(),
                    modifier = Modifier.fillMaxWidth(),
                    showError = showLonError,
                    errorMessage = lonErrorMessage,
                    onFocus = { showLonError = false },
                    onLeaveFocus = {
                        lonErrorMessage = validateLongitude(longitude)
                        if (!lonErrorMessage.isNullOrEmpty()) {
                            showLonError = true
                        }
                    }
                )
            }
            Column (
                Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = PetClinicAppointmentTheme.dimensions.grid_2
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = PetClinicAppointmentTheme.dimensions.grid_2)
                )
                AppButton(
                    onClick = { onClickSignUp() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PetClinicAppointmentTheme.dimensions.grid_5_5)
                        .padding(
                            start = PetClinicAppointmentTheme.dimensions.grid_2,
                            end = PetClinicAppointmentTheme.dimensions.grid_2,
                            bottom = PetClinicAppointmentTheme.dimensions.grid_1
                        ),
                    colors = buttonColors(PetClinicAppointmentTheme.colors.secondary),
                    shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                ) {
                    Text(
                        text = "Daftar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
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
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(visible = progressIndicatorVisible)
        }
    }
}

@Composable
fun LocationInputs(
    registerViewModel: RegisterViewModel,
    containerModifier: Modifier = Modifier,
    modifier: Modifier = Modifier
){
    val coroutineScope = rememberCoroutineScope()
    var inputType by rememberSaveable { mutableStateOf("province") }
    val locationOptionList by registerViewModel.locationOptionList.collectAsState()
    var locationValue by rememberSaveable { mutableStateOf("") }
    val selectedProvinceId by registerViewModel.selectedProvinceId.collectAsState()
    val selectedCityId by registerViewModel.selectedCityId.collectAsState()
    val selectedDistrictId by registerViewModel.selectedDistrictId.collectAsState()

    fun initLocationInput(){
        inputType = "province"
        locationValue = ""
    }

    LaunchedEffect(inputType){
        when(inputType){
            "province" -> registerViewModel.getProvinceList()
            "city" -> selectedProvinceId?.let { registerViewModel.getCityList(it) }
            "district" -> selectedCityId?.let { registerViewModel.getDistrictList(it) }
            "village" -> {
                selectedDistrictId?.let { registerViewModel.getVillageList(it) }
            }
        }
    }

    DropdownTextInput(
        option = DropdownOption("", locationValue),
        optionList = locationOptionList,
        onValueChange = {},
        placeholder = "Kelurahan, Kecamatan, Kota/Kabupaten, dan Provinsi",
        onCancel = { initLocationInput() },
        onClickOption = { option ->
            locationValue = if(inputType == "province") option.value else "${option.value}, $locationValue"
            coroutineScope.launch {
                when(inputType){
                    "province" -> {
                        inputType = "city"
                        registerViewModel.setSelectedProvinceId(option.id.toLong())
                    }
                    "city" -> {
                        inputType = "district"
                        registerViewModel.setSelectedCityId(option.id.toLong())
                    }
                    "district" -> {
                        inputType = "village"
                        registerViewModel.setSelectedDistrictId(option.id.toLong())
                    }
                    "village" -> {
                        registerViewModel.setSelectedVillageId(option.id.toLong())
                        registerViewModel.setLocationOptionList(listOf())
                    }
                }
            }
        },
        showEmptyListError = false,
        dismissEnabled = false,
        autoCollapseMenuEnabled = false,
        containerModifier = containerModifier,
        modifier = modifier
    )
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

fun validatePhoneNum(phoneNum: String): String? {
    if(phoneNum.isEmpty()){
        return "No. telp wajib diinput."
    }
    return null
}

fun validateAddress(address: String): String? {
    if(address.isEmpty()){
        return "Alamat wajib diinput."
    }
    return null
}

fun validateLatitude(latitude: String): String? {
    if(latitude.isEmpty()){
        return "Lintang wajib diinput."
    }
    return null
}

fun validateLongitude(longitude: String): String? {
    if(longitude.isEmpty()){
        return "Bujur wajib diinput."
    }
    return null
}


