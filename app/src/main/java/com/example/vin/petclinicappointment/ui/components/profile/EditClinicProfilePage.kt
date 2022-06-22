package com.example.vin.petclinicappointment.ui.components.profile

import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.ui.components.common.*
import com.example.vin.petclinicappointment.ui.components.common.Image
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.regex.Pattern
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.ui.text.input.KeyboardType
import com.example.vin.petclinicappointment.ui.theme.Black_50
import java.util.jar.Manifest

@Composable
fun EditClinicProfilePage(
    editClinicProfileViewModel: EditClinicProfileViewModel = hiltViewModel(),
    navigateToProfile: () -> Unit,
    navigateBack: () -> Unit,
    scaffoldState: ScaffoldState
){
    val localFocusManager = LocalFocusManager.current
    val context = LocalContext.current
    val readStoragePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
    val clinicName by editClinicProfileViewModel.clinicName.collectAsState()
    val clinicEmail by editClinicProfileViewModel.clinicEmail.collectAsState()
    val initialClinicImage by editClinicProfileViewModel.initialClinicImage.collectAsState()
    val clinicImage by editClinicProfileViewModel.clinicImage.collectAsState()
    val clinicPhoneNumber by editClinicProfileViewModel.clinicPhoneNum.collectAsState()
    val clinicAddress by editClinicProfileViewModel.clinicAddress.collectAsState()
    val clinicLatitude by editClinicProfileViewModel.clinicLatitude.collectAsState()
    val clinicLongitude by editClinicProfileViewModel.clinicLongitude.collectAsState()

    var showNameError by rememberSaveable { mutableStateOf(false)}
    var showEmailError by rememberSaveable { mutableStateOf( false )}
    var showPhoneNumError by rememberSaveable { mutableStateOf(false)}
    var showAddressError by rememberSaveable { mutableStateOf(false)}
    var showLatError by rememberSaveable { mutableStateOf(false)}
    var showLonError by rememberSaveable { mutableStateOf(false)}

    var nameErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var emailErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var phoneNumErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var addressErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var latErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}
    var lonErrorMessage by rememberSaveable { mutableStateOf<String?>( null )}

    var progressIndicatorVisible by rememberSaveable { mutableStateOf(false) }

    val getImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){ uri: Uri? ->
        editClinicProfileViewModel.setClinicImage(uri)
    }

    fun onReadStoragePermissionGranted() {
        getImageLauncher.launch("image/*")
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionGranted ->
        if (permissionGranted) {
            onReadStoragePermissionGranted()
        }
    }

    LaunchedEffect(Unit){
        editClinicProfileViewModel.message.collectLatest {
            if(it.isNotEmpty()) {
                scaffoldState.snackbarHostState.showSnackbar(it)
            }
        }
    }

    LaunchedEffect(Unit){
        progressIndicatorVisible = true
        editClinicProfileViewModel.getClinicData()
        editClinicProfileViewModel.getVillageDetail()
        editClinicProfileViewModel.getDistrictDetail()
        editClinicProfileViewModel.getCityDetail()
        editClinicProfileViewModel.getProvinceDetail()
        progressIndicatorVisible = false
    }

    fun onClickEdit(){
        runBlocking {
            progressIndicatorVisible = true
            val isSuccess = editClinicProfileViewModel.updateClinic(context)
            progressIndicatorVisible = false
            if(isSuccess){
                editClinicProfileViewModel.saveClinic()
                navigateToProfile()
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
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
                    "Ubah Profil Klinik",
                    style = PetClinicAppointmentTheme.typography.h1
                )
            }
            if(!progressIndicatorVisible) {
                Column(
                    Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    View(
                        Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                            .fillMaxWidth()
                            .height(PetClinicAppointmentTheme.dimensions.grid_5 * 5),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()){
                            drawRoundRect(
                                color = Color.Black,
                                style = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 10f))
                            )
                        }
                        Column (
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.SpaceBetween
                        ){
                            View(
                                Modifier.background(PetClinicAppointmentTheme.colors.error),
                                onClick = {
                                    editClinicProfileViewModel.setClinicImage(null)
                                    editClinicProfileViewModel.setInitialClinicImage("")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete icon",
                                    modifier = Modifier.padding(PetClinicAppointmentTheme.dimensions.grid_1),
                                    tint = PetClinicAppointmentTheme.colors.onError
                                )
                            }
                            View(
                                Modifier.background(Black_50),
                                onClick = { permissionLauncher.launch(readStoragePermission) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChangeCircle,
                                    contentDescription = "change icon",
                                    modifier = Modifier.padding(PetClinicAppointmentTheme.dimensions.grid_1),
                                    tint = Color.White
                                )
                            }
                        }
                        if (clinicImage !== null) {
                            Image(
                                painter = rememberAsyncImagePainter(model = clinicImage),
                                contentDescription = "clinic image",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else if (initialClinicImage.isNotEmpty()) {
                            Image(
                                base64 = initialClinicImage,
                                contentDescription = "clinic image",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Text("Foto klinik tidak tersedia")
                        }
                    }
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
                        value = clinicName,
                        onValueChange = { editClinicProfileViewModel.setClinicName(it) },
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
                            nameErrorMessage = validateUserName(clinicName)
                            if (!nameErrorMessage.isNullOrEmpty()) {
                                showNameError = true
                            }
                        }
                    )
                    TextInput(
                        value = clinicEmail,
                        onValueChange = { editClinicProfileViewModel.setClinicEmail(it) },
                        placeholder = "Email",
                        containerModifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                            .fillMaxWidth(),
                        modifier = Modifier.fillMaxWidth(),
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        showError = showEmailError,
                        errorMessage = emailErrorMessage,
                        onFocus = { showEmailError = false },
                        onLeaveFocus = {
                            emailErrorMessage = validateEmail(clinicEmail)
                            if (!emailErrorMessage.isNullOrEmpty()) {
                                showEmailError = true
                            }
                        }
                    )
                    TextInput(
                        value = clinicPhoneNumber,
                        onValueChange = { editClinicProfileViewModel.setClinicPhoneNum(it) },
                        placeholder = "No. Telp",
                        containerModifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_4
                            )
                            .fillMaxWidth(),
                        modifier = Modifier.fillMaxWidth(),
                        numberOnly = true,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        showError = showPhoneNumError,
                        errorMessage = phoneNumErrorMessage,
                        onFocus = { showPhoneNumError = false },
                        onLeaveFocus = {
                            phoneNumErrorMessage = validatePhoneNum(clinicPhoneNumber)
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
                        value = clinicAddress,
                        onValueChange = { editClinicProfileViewModel.setClinicAddress(it) },
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
                            addressErrorMessage = validateAddress(clinicAddress)
                            if (!addressErrorMessage.isNullOrEmpty()) {
                                showAddressError = true
                            }
                        }
                    )
                    LocationInputs(
                        editClinicProfileViewModel = editClinicProfileViewModel,
                        containerModifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                            .fillMaxWidth()
                    )
                    TextInput(
                        value = clinicLatitude,
                        onValueChange = { editClinicProfileViewModel.setClinicLatitude(it) },
                        placeholder = "Lintang",
                        containerModifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_2
                            )
                            .fillMaxWidth(),
                        modifier = Modifier.fillMaxWidth(),
                        numberWithDotAndHyphenOnly = true,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        showError = showLatError,
                        errorMessage = latErrorMessage,
                        onFocus = { showLatError = false },
                        onLeaveFocus = {
                            latErrorMessage = validateLatitude(clinicLatitude)
                            if (!latErrorMessage.isNullOrEmpty()) {
                                showLatError = true
                            }
                        }
                    )
                    TextInput(
                        value = clinicLongitude,
                        onValueChange = { editClinicProfileViewModel.setClinicLongitude(it) },
                        placeholder = "Bujur",
                        containerModifier = Modifier
                            .padding(
                                start = PetClinicAppointmentTheme.dimensions.grid_2,
                                end = PetClinicAppointmentTheme.dimensions.grid_2,
                                bottom = PetClinicAppointmentTheme.dimensions.grid_4
                            )
                            .fillMaxWidth(),
                        modifier = Modifier.fillMaxWidth(),
                        numberWithDotAndHyphenOnly = true,
                        keyBoardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        showError = showLonError,
                        errorMessage = lonErrorMessage,
                        onFocus = { showLonError = false },
                        onLeaveFocus = {
                            lonErrorMessage = validateLongitude(clinicLongitude)
                            if (!lonErrorMessage.isNullOrEmpty()) {
                                showLonError = true
                            }
                        }
                    )
                    Column {
                        Divider(Modifier.fillMaxSize())
                        AppButton(
                            onClick = { onClickEdit() },
                            modifier = Modifier
                                .padding(PetClinicAppointmentTheme.dimensions.grid_2)
                                .fillMaxWidth()
                                .height(PetClinicAppointmentTheme.dimensions.grid_5_5),
                            colors = ButtonDefaults.buttonColors(PetClinicAppointmentTheme.colors.primary),
                            shape = RoundedCornerShape(PetClinicAppointmentTheme.dimensions.grid_5)
                        ) {
                            Text("Ubah Profil")
                        }
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

@Composable
fun LocationInputs(
    editClinicProfileViewModel: EditClinicProfileViewModel,
    containerModifier: Modifier = Modifier,
    modifier: Modifier = Modifier
){
    val coroutineScope = rememberCoroutineScope()
    var inputType by rememberSaveable { mutableStateOf("province") }
    val locationOptionList by editClinicProfileViewModel.locationOptionList.collectAsState()
    var locationValue by rememberSaveable { mutableStateOf("") }
    val selectedVillageOption by editClinicProfileViewModel.selectedVillageOption.collectAsState()
    val selectedDistrictOption by editClinicProfileViewModel.selectedDistrictOption.collectAsState()
    val selectedCityOption by editClinicProfileViewModel.selectedCityOption.collectAsState()
    val selectedProvinceOption by editClinicProfileViewModel.selectedProvinceOption.collectAsState()

    fun initLocationInput(){
        inputType = "province"
        editClinicProfileViewModel.setSelectedVillageOption(null)
        editClinicProfileViewModel.setSelectedDistrictOption(null)
        editClinicProfileViewModel.setSelectedCityOption(null)
        editClinicProfileViewModel.setSelectedProvinceOption(null)
    }

    LaunchedEffect(Unit){
        locationValue = "${selectedVillageOption?.value}, ${selectedDistrictOption?.value}, ${selectedCityOption?.value}, ${selectedProvinceOption?.value}"
    }

    LaunchedEffect(selectedVillageOption, selectedDistrictOption, selectedCityOption, selectedProvinceOption){
        if(selectedVillageOption == null &&
            selectedDistrictOption == null &&
            selectedCityOption == null &&
            selectedProvinceOption == null
        ) { locationValue = "" }
    }

    LaunchedEffect(inputType){
        when(inputType){
            "province" -> editClinicProfileViewModel.getProvinceList()
            "city" -> selectedProvinceOption?.id?.let { editClinicProfileViewModel.getCityList(it.toLong()) }
            "district" -> selectedCityOption?.id?.let { editClinicProfileViewModel.getDistrictList(it.toLong()) }
            "village" -> selectedDistrictOption?.id?.let { editClinicProfileViewModel.getVillageList(it.toLong()) }
        }
    }

    DropdownTextInput(
        option = DropdownOption("", locationValue),
        optionList = locationOptionList,
        onValueChange = {},
        placeholder = "Kelurahan, Kecamatan, Kota/Kabupaten, dan Provinsi",
        readOnly = true,
        onCancel = { initLocationInput() },
        onClickOption = { option ->
            locationValue = if(inputType == "province") option.value else "${option.value}, $locationValue"
            coroutineScope.launch {
                when(inputType){
                    "province" -> {
                        inputType = "city"
                        editClinicProfileViewModel.setSelectedProvinceOption(option)
                    }
                    "city" -> {
                        inputType = "district"
                        editClinicProfileViewModel.setSelectedCityOption(option)
                    }
                    "district" -> {
                        inputType = "village"
                        editClinicProfileViewModel.setSelectedDistrictOption(option)
                    }
                    "village" -> {
                        editClinicProfileViewModel.setSelectedVillageOption(option)
                        editClinicProfileViewModel.setLocationOptionList(listOf())
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
