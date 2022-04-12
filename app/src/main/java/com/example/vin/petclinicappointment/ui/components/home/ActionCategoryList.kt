package com.example.vin.petclinicappointment.ui.components.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vin.petclinicappointment.R
import kotlinx.coroutines.launch
import com.example.vin.petclinicappointment.ui.utils.rememberWindowInfo

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActionCategoryList(
    modalBottomSheetState: ModalBottomSheetState,
    selectedActionCategoryListState: MutableState<String>,
    title: String,
    categoryList: List<ActionCategory>,
    navigateToPage: (route: String) -> Unit,
    baseRoute: String
) {
    val windowInfo = rememberWindowInfo()
    val coroutineScope = rememberCoroutineScope()
    val windowWidth = windowInfo.width.size.toString().substringBefore(".dp").toFloat().toInt()
    val rowItemCount = (windowWidth + (windowWidth/2))/100

    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    bottom = 20.dp,
                    top = 20.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
                ) {
            Text(
                title,
                Modifier.padding(bottom = 5.dp),
                style = MaterialTheme.typography.h1
            )
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        selectedActionCategoryListState.value = title
                        modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                    }
            }) {
                Icon(
                    imageVector = Icons.Default.NavigateNext,
                    contentDescription = "more"
                )
            }
        }
        LazyRow(
            Modifier.padding(start = 20.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                items(
                    if(categoryList.size < rowItemCount) categoryList
                    else categoryList.subList(0, rowItemCount-1)
                ) { actionCategory ->
                    ActionCategoryItem(
                        actionCategory
                    ) {
                        navigateToPage("$baseRoute/${actionCategory.title}/${actionCategory.type}")
                    }
                }
        }
    }
}

@Composable
fun ActionCategoryItem(
    actionCategory: ActionCategory,
    navigateToPage: () -> Unit
){
    Column(
        Modifier.width(110.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            Modifier
                .padding(bottom = 10.dp)
                .size(110.dp)
                .clickable { navigateToPage() },
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(10.dp),
            elevation = 8.dp
        ) {
            Image(
                painter = painterResource(id = actionCategory.image),
                contentDescription = actionCategory.description,
                contentScale = ContentScale.Fit
            )
        }
        Text(
            actionCategory.title,
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center
        )
    }
}