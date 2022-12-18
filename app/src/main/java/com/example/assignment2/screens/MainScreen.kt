package com.example.assignment2.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment2.R
import com.example.assignment2.ui.theme.Assignment2Theme
import com.example.assignment2.viewmodel.BoredActivityItemViewModel
import com.example.assignment2.viewmodel.MainViewModel
import com.example.assignment2.viewmodel.MainViewModel.Companion.ALL_CATEGORIES

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val items by viewModel.activities.observeAsState(emptyList())
    Scaffold(topBar = { TopBar(viewModel) },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_padding)),
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.space_padding))
                ) {
                    items.map { item { OfferListItem(viewModel = it) } }
                }
            }
        })
}

@Composable
fun TopBar(viewModel: MainViewModel) {

    var toolbarTitle by remember { mutableStateOf(ALL_CATEGORIES[0]) }
    var displayMenu by remember { mutableStateOf(false) }
    TopAppBar(
        elevation = 4.dp,
        title = {
            Text(toolbarTitle)
        },
        backgroundColor = MaterialTheme.colors.primary,
        actions = {

            IconButton(onClick = { displayMenu = true }) {
                Icon(Icons.Filled.MoreVert, null)
            }
            DropdownMenu(expanded = displayMenu, onDismissRequest = { displayMenu = false }) {
                ALL_CATEGORIES.map {
                    it.takeIf { it != toolbarTitle }?.let {
                        DropdownMenuItem(onClick = {
                            displayMenu = false
                            toolbarTitle = it
                            viewModel.getRandomActivities.invoke(it)
                        }) {
                            Text(text = it)
                        }
                    }
                }
            }
        })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OfferListItem(viewModel: BoredActivityItemViewModel) {
    Surface(
        shape = MaterialTheme.shapes.small,
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = {}, onLongClick = {
            })
    ) {
        Box {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.medium_padding))
            ) {
                Text(text = viewModel.boredActivity.activity, fontSize = 30.sp)
                Row {
                    Text(text = "Accessibility: ", fontSize = 16.sp, fontStyle = FontStyle.Italic)
                    Text(text = viewModel.boredActivity.accessibility.toString(), fontSize = 16.sp)
                }
                Row {
                    Text(text = "Type: ", fontSize = 16.sp, fontStyle = FontStyle.Italic)
                    Text(text = viewModel.boredActivity.type, fontSize = 16.sp)
                }
                Row {
                    Text(text = "Participants: ", fontSize = 16.sp, fontStyle = FontStyle.Italic)
                    Text(text = viewModel.boredActivity.participants.toString(), fontSize = 16.sp)
                }
                Row {
                    Text(text = "Price: ", fontSize = 16.sp, fontStyle = FontStyle.Italic)
                    Text(text = viewModel.boredActivity.price.toString(), fontSize = 16.sp)
                }
                Row {
                    Text(text = "Link: ", fontSize = 16.sp, fontStyle = FontStyle.Italic)
                    Text(text = viewModel.boredActivity.link, fontSize = 16.sp)
                }
                Row {
                    Text(text = "Key: ", fontSize = 16.sp, fontStyle = FontStyle.Italic)
                    Text(text = viewModel.boredActivity.key, fontSize = 16.sp)
                }

            }


            /*
            Box {
                Image(
                    painter = painterResource(id = viewModel.image),
                    contentDescription = "image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                Text(
                    text = viewModel.price.toString() + " EUR",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
             */
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Assignment2Theme {
        MainScreen(MainViewModel())
    }
}