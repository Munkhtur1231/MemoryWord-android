package com.example.memorizeword.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.memorizeword.MemorizeWordTopAppBar
import com.example.memorizeword.R
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun OptionScreen(
    modifier: Modifier = Modifier,
    viewModel: MemorizeWordViewModel = viewModel(factory = MemorizeWordViewModel.factory),
    onNextButtonClicked: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(topBar = {
        MemorizeWordTopAppBar(
            title = stringResource(R.string.top_bar_title),
            canNavigateBack = false,
            navigateUp = navigateUp
        )
    }) { innerPadding ->

        val wordUiState by viewModel.wordUiState.collectAsState()

        val radioOptions = listOf("english", "mongolian", "both")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(wordUiState.wordOption) }

        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Үгийн тохиргоо",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                viewModel.changeWordOption(text)
                                onOptionSelected(text)
                            },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 50.dp),
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                            viewModel.changeWordOption(text)
                        }
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            Button(
                onClick = onNextButtonClicked,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "Хадгалах",
                )
            }
        }
    }
}
