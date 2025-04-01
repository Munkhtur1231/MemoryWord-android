package com.example.memorizeword.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.memorizeword.MemorizeWordTopAppBar
import com.example.memorizeword.R

@Composable
fun WordScreen(
    modifier: Modifier = Modifier,
    viewModel: MemorizeWordViewModel = viewModel(factory = MemorizeWordViewModel.factory),
    onAddButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    navigateUp: () -> Unit,
) {
    val wordUiState by viewModel.wordUiState.collectAsState()
    var isDeleteClicked by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        MemorizeWordTopAppBar(
            title = stringResource(R.string.top_bar_title),
            canNavigateBack = true,
            navigateUp = navigateUp
        )
    }) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                if (wordUiState.currentWord != null) {
                    WordView(
                        value = wordUiState.currentWord!!.english,
                        onClicked = { viewModel.toggleEnglishVisibility() },
                        isShow = wordUiState.isEnglishShow,
                        modifier = Modifier
                            .background(Color.Black, shape = MaterialTheme.shapes.small)
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    WordView(
                        value = wordUiState.currentWord!!.mongolian,
                        onClicked = { viewModel.toggleMongolianVisibility() },
                        isShow = wordUiState.isMongolianShow,
                        modifier = Modifier
                            .background(Color.Black, shape = MaterialTheme.shapes.small)
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                }
                Row(modifier = Modifier.padding(top = 100.dp, bottom = 50.dp)) {
                    Button(onClick = onAddButtonClicked) {
                        Text(
                            text = "Add Word"
                        )
                    }
                    if (wordUiState.currentWord != null) {
                        Button(onClick = onEditButtonClicked) {
                            Text(
                                text = "Edit Word"
                            )
                        }
                        Button(onClick = { isDeleteClicked = true }) {
                            Text(
                                text = "Delete Word"
                            )
                        }
                    }
                }
                if (wordUiState.currentWord != null) {
                    Row {
                        Button(onClick = { viewModel.previousWord() }) {
                            Text(
                                text = "Previous Word"
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = { viewModel.nextWord() }) {
                            Text(
                                text = "Next Word"
                            )
                        }
                    }
                }
            }
            if (isDeleteClicked) {
                DeleteDialogueBox(
                    onDeleteClicked = { viewModel.deleteWord() },
                    onDismissClicked = { isDeleteClicked = false },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

    }
}

@Composable
fun WordView(
    value: String, onClicked: () -> Unit, isShow: Boolean, modifier: Modifier = Modifier
) {
    Text(
        text = if (isShow) value else " ",
        color = Color.White,
        fontSize = 42.sp,
        modifier = modifier.clickable { onClicked() })
}

@Composable
fun DeleteDialogueBox(
    onDeleteClicked: () -> Unit, onDismissClicked: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(Color.Gray, shape = MaterialTheme.shapes.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Үгийг устгах уу?",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Row {
            Button(onClick = onDismissClicked) {
                Text(text = "Үгүй")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                onDeleteClicked()
                onDismissClicked()
            }) {
                Text(text = "Тийм")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    WordView(
        value = "Hello",
        onClicked = {},
        isShow = true,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.Black)
    )
}