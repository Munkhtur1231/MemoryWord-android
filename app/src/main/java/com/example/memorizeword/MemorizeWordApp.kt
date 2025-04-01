package com.example.memorizeword

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.memorizeword.ui.screens.AddWordScreen
import com.example.memorizeword.ui.screens.EditWordScreen
import com.example.memorizeword.ui.screens.MemorizeWordViewModel
import com.example.memorizeword.ui.screens.OptionScreen
import com.example.memorizeword.ui.screens.WordScreen

enum class MemorizeAppScreen(@StringRes val title: Int) {
    Start(title = R.string.options),
    Main(title = R.string.app_name),
    Add(title = R.string.add_word),
    Edit(title = R.string.edit_word)
}

@Composable
fun MemorizeWordApp(
    navController: NavHostController = rememberNavController(),
    viewModel: MemorizeWordViewModel = viewModel(factory = MemorizeWordViewModel.factory)
) {
    NavHost(
        navController = navController,
        startDestination = MemorizeAppScreen.Start.name,
        modifier = Modifier
            .fillMaxSize()
    ) {
        composable(route = MemorizeAppScreen.Start.name) {
            OptionScreen(
                onNextButtonClicked = {
                    navController.navigate(
                        MemorizeAppScreen.Main.name
                    )
                },
                navigateUp = { navController.navigateUp() },
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
        composable(route = MemorizeAppScreen.Main.name) {
            WordScreen(
                onAddButtonClicked = {
                    navController.navigate(
                        MemorizeAppScreen.Add.name
                    )
                },
                onEditButtonClicked = {
                    navController.navigate(
                        MemorizeAppScreen.Edit.name
                    )
                },
                viewModel = viewModel,
                navigateUp = { navController.navigateUp() },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
        composable(route = MemorizeAppScreen.Add.name) {
            AddWordScreen(
                navigateUp = { navController.navigateUp() },
                viewModel = viewModel,
            )
        }
        composable(route = MemorizeAppScreen.Edit.name) {
            EditWordScreen(
                navigateUp = { navController.navigateUp() },
                viewModel = viewModel,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemorizeWordTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}