@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package com.example.tolaaleksey.weatherinfo.screens

import android.annotation.SuppressLint
import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tolaaleksey.weatherinfo.R
import com.example.tolaaleksey.weatherinfo.classes.Day
import com.example.tolaaleksey.weatherinfo.classes.HomeViewModule
import com.example.tolaaleksey.weatherinfo.classes.Weather
import io.ktor.util.reflect.instanceOf
import kotlin.reflect.KFunction1

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
    val viewModel = viewModel<HomeViewModule>()
    Scaffold(
        floatingActionButton = { FloatingActionButtonCompose(viewModel) },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("AboutScreen") {
                            popUpTo("AboutScreen") {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "Navigation Menu",
                            tint = Color.White
                        )
                    }
                },
                title = {
                    Text(
                        stringResource(R.string.home),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 24.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xff1b1b23),
                )
            )
        },
        containerColor = Color(0xff1b1b23),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            HomeScreenContent(
                items = viewModel.days,
                onRemove = viewModel::onClickRemoveDay,
                onAdd = { navController.navigate("HomeScreen") }
            )

        }
        FloatingActionButton(onClick = { }, contentColor = Color.White) {
        }
    }
}

@Composable
fun FloatingActionButtonCompose(viewModule: HomeViewModule) {
    FloatingActionButton(onClick = { /*TODO*/ }) {
        IconButton(onClick = {
            val weather = Weather(6, 80, 34, 60)
            val day = Day(
                weather, "Это описание, братуха"
            )
            viewModule.days.add(day)
        }
        ) {
            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
        }
    }
}

@Composable
fun HomeScreenContent(
    items: List<Day>,
    onRemove: (Day) -> Unit,
    onAdd: () -> Unit,
) {
    val context = LocalContext.current
    Column() {
        items.forEach {
            DayItem(
                day = it,
                onRemove = {
                    onRemove(it)
                    Toast.makeText(context, "You are delete a day", Toast.LENGTH_SHORT)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

    }
}

@Composable
fun DayItem(
    day: Day,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier) {
        Column(modifier = Modifier.fillMaxHeight()) {
            Text(text = "Temperature: ${day.weather.temperature} С", color = Color.White)
            Text(text = "Cloudiness: ${day.weather.cloudiness}%", color = Color.White)
            Text(text = "Chance of rain: ${day.weather.chanceOfRain}%", color = Color.White)
            Text(text = "Humidity: ${day.weather.humidity}%", color = Color.White)
        }

        Column(modifier = Modifier
            .fillMaxHeight()
            .width(120.dp)
            .height(100.dp)) {
            Text(text = "Description:", color = Color.White)
            Text(text = day.description, color = Color.White)
        }

        Row(modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onRemove() }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = stringResource(R.string.delete),
                    tint = Color.White
                )
            }

            IconButton(onClick = { onRemove() }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = Color.Red
                )
            }
        }
    }
}

@Preview
@Composable
fun preview() {
    val navController = rememberNavController()
    Home(navController = navController)
}

