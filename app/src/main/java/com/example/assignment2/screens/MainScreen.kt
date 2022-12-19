package com.example.assignment2.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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
    val loading by viewModel.loading.observeAsState(false)

    Scaffold(topBar = { TopBar(viewModel) },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Box {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items.map { item { BoredItem(viewModel = it) } }
                    }
                    if (loading) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
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
fun BoredItem(viewModel: BoredActivityItemViewModel) {
    val favourite by viewModel.showFavourite.observeAsState(false)
    val isSent by viewModel.showSent.observeAsState(false)
    var displayMenu by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }

    Surface(
        shape = MaterialTheme.shapes.small,
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.space_padding))
            .combinedClickable(onClick = {
                viewModel.onClick.invoke()
            }, onLongClick = {
                displayMenu = true
            })
    ) {

        DropdownMenu(expanded = displayMenu, onDismissRequest = { displayMenu = false }) {
            DropdownMenuItem(onClick = {
                displayMenu = false
                showDialog.value = true
            }) {
                Text(text = "Send to a Friend")
            }
        }

        ItemAlertDialog(showDialog, viewModel)

        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (!isSent) MaterialTheme.colors.secondary else colorResource(id = R.color.purple_200))
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
            if (favourite) {
                Icon(
                    Icons.Filled.Star, contentDescription = null, modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
    }
}

@Composable
fun ItemAlertDialog(openDialog: MutableState<Boolean>, selectedViewModel: BoredActivityItemViewModel) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Send \"" + selectedViewModel.boredActivity.activity + "\" to a friend?")
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(start = 20.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
                ) {
                    Button(
                        modifier = Modifier.padding(end = 5.dp),
                        onClick = {
                            selectedViewModel.onSend.invoke()
                            openDialog.value = false
                        }
                    ) {
                        Text("Yes")
                    }
                    Button(
                        onClick = { openDialog.value = false }
                    ) {
                        Text("No")
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Assignment2Theme {
        MainScreen(MainViewModel())
    }
}