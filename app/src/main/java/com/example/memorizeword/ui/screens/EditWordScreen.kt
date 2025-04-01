package com.example.memorizeword.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.memorizeword.MemorizeWordTopAppBar
import com.example.memorizeword.R

@Composable
fun EditWordScreen(
    modifier: Modifier = Modifier,
    viewModel: MemorizeWordViewModel = viewModel(factory = MemorizeWordViewModel.factory),
    navigateUp: () -> Unit
) {
    val wordUiState by viewModel.wordUiState.collectAsState()

    viewModel.replaceInputByCurrentWord()

    Scaffold(topBar = {
        MemorizeWordTopAppBar(
            title = stringResource(R.string.edit_word),
            canNavigateBack = true,
            navigateUp = navigateUp
        )
    }) { innerPadding ->
        Column(
            modifier = modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = viewModel.userEnglishInput,
                onValueChange = {
                    viewModel.updateUserEnglishInput(it)
                },
                placeholder = { Text(text = "Гадаад үг") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            TextField(
                value = viewModel.userMongolianInput,
                onValueChange = {
                    viewModel.updateUserMongolianInput(it)
                },
                placeholder = { Text(text = "Монгол үг") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.editWord()
                    navigateUp()
                },
            ) {
                Text(
                    text = "Шинэчлэх"
                )
            }
        }
    }
}