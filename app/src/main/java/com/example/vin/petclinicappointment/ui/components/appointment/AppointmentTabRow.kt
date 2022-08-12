package com.example.vin.petclinicappointment.ui.components.appointment

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LeadingIconTab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.example.vin.petclinicappointment.ui.theme.PetClinicAppointmentTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppointmentTabRow(
    tabs: List<AppointmentTab>,
    pagerState: PagerState
){
    val coroutineScope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = { positions ->
            TabRowDefaults.Indicator(
                Modifier
                    .pagerTabIndicatorOffset(pagerState, positions)
                    .clip(RoundedCornerShape(50)),
                height = PetClinicAppointmentTheme.dimensions.grid_0_25,
                color = PetClinicAppointmentTheme.colors.primary
            )
        },
        backgroundColor = PetClinicAppointmentTheme.colors.surface
    ) {
        tabs.forEachIndexed { idx, tab ->
            val title = stringResource(tab.title)
            val selected = pagerState.currentPage == idx
            LeadingIconTab(
                selected = selected,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(idx)
                    }
                },
                text = {
                    Text(
                        title,
                        style = PetClinicAppointmentTheme.typography.h2,
                        color = PetClinicAppointmentTheme.colors.primary
                    )
                       },
                icon = {},
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AppointmentTabContent(
    tabs: List<AppointmentTab>,
    pagerState: PagerState,
    navigateToAppointmentDetail: (id: Int) -> Unit
){
    HorizontalPager(count = tabs.size, state = pagerState) { idx ->
        tabs[idx].view(navigateToAppointmentDetail, pagerState.currentPage)
    }
}