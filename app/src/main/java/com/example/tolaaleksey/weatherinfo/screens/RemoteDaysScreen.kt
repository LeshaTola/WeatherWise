package com.example.tolaaleksey.weatherinfo.screens

import LoadingAnimation
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tolaaleksey.weatherinfo.Interfaces.HomeState
import com.example.tolaaleksey.weatherinfo.R
import com.example.tolaaleksey.weatherinfo.classes.Day
import com.example.tolaaleksey.weatherinfo.classes.RemoteDaysState
import com.example.tolaaleksey.weatherinfo.classes.RemoteDaysViewModel
import com.example.tolaaleksey.weatherinfo.classes.Weather
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

import java.util.*

@Composable
fun RemoteDaysScreen(navController: NavController) {
    val viewModel = koinInject<RemoteDaysViewModel>()

    val myState: State<RemoteDaysState> = viewModel.state.collectAsStateWithLifecycle()
    RemoteDaysScreenContent(
        myState.value,
        navController = navController,
        onAdd = { day ->
            viewModel.viewModelScope.launch {
                viewModel.onClickAddDay(day);
                navController.popBackStack()
            }
        }
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RemoteDaysScreenContent(
    value: RemoteDaysState,
    navController: NavController,
    onAdd: (day: Day) -> Unit
) {
    Scaffold(
        topBar = { RemoteDaysTopAppBar(navController) },
        containerColor = Color(0xff1b1b23),
    ) { values ->
        when (value) {
            is RemoteDaysState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingAnimation(circleColor = Color.White)
                }
            }

            is RemoteDaysState.DisplayingDays -> LazyColumn(modifier = Modifier.padding(values)) {
                itemsIndexed(items = value.days) { _, item ->
                    RemoteDayItem(
                        day = item,
                        onAdd = onAdd,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 5.dp),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoteDaysTopAppBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = "Remote Days",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Navigation Menu",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.night_blue),
        ),
    )
}

@Composable
fun RemoteDayItem(
    day: Day,
    onAdd: (day: Day) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current;
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(2.dp)
        ) {
            Text(text = "Temperature: ${day.weather.temperature} С", color = Color.White)
            Text(text = "Cloudiness: ${day.weather.cloudiness}%", color = Color.White)
            Text(text = "Chance of rain: ${day.weather.chanceOfRain}%", color = Color.White)
            Text(text = "Humidity: ${day.weather.humidity}%", color = Color.White)
        }

        IconButton(onClick = {
            onAdd(day)
            Toast.makeText(context, "You are add note", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                Icons.Default.AddCircle,
                contentDescription = null,
                tint = Color.Green,
                modifier = Modifier.fillMaxSize(1f),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RemoteDaysPreview() {
    val navController = rememberNavController()
    //RemoteDaysScreenContent(listOf(Day(Weather(0, 0, 0, 0), "")), navController)
}