package com.example.vin.petclinicappointment.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vin.petclinicappointment.R
import com.example.vin.petclinicappointment.data.actionCategoryList

@Composable
fun MoreCategoryContent(
    title: String
) {
    Column(
        Modifier.fillMaxSize(),
    ) {
        Text(
            title,
            Modifier.padding(
                start = 20.dp,
                bottom = 20.dp
            ),
            style = MaterialTheme.typography.h1
        )
        ServiceItemList()
    }
}

@Composable
fun ServiceItemList(){
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        items(actionCategoryList) { actionCategory ->
            ServiceItem(actionCategory)
        }
    }
}

@Composable
fun ServiceItem(actionCategory: ActionCategory){
    Row (
        Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.register_success),
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
        Column {
            Text(
                actionCategory.title,
                style = MaterialTheme.typography.h2,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Text(
                actionCategory.description,
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
            )
        }
    }
}