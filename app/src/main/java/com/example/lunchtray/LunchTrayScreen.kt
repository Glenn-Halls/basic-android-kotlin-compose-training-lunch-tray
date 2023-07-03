/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.datasource.DataSource.accompanimentMenuItems
import com.example.lunchtray.datasource.DataSource.entreeMenuItems
import com.example.lunchtray.datasource.DataSource.sideDishMenuItems
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.OrderViewModel
import com.example.lunchtray.ui.SideDishMenuScreen
import com.example.lunchtray.ui.StartOrderScreen

enum class LunchTrayAppScreen(@StringRes val title: Int) {
    StartScreen(R.string.app_name),
    EntreeScreen(R.string.choose_entree),
    SideDishScreen(R.string.choose_side_dish),
    AccompanimentScreen(R.string.choose_accompaniment),
    CheckoutScreen(R.string.order_checkout)
}

// TODO: AppBar
@Composable
fun LunchTrayAppBar(
    currentScreen: LunchTrayAppScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}


@Composable
fun LunchTrayApp(
    navController: NavHostController = rememberNavController()
) {
    // Create Controller and initialization
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = LunchTrayAppScreen.valueOf(
        backStackEntry?.destination?.route ?: LunchTrayAppScreen.StartScreen.name
    )

    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()

    // Helper function navigates to start screen
    fun navigateToStart() {
        viewModel.resetOrder()
        navController.popBackStack(LunchTrayAppScreen.StartScreen.name, inclusive = false)
    }

    Scaffold(
        topBar = {
            LunchTrayAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        // TODO: Navigation host
        NavHost(
            navController = navController,
            startDestination = LunchTrayAppScreen.StartScreen.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = LunchTrayAppScreen.StartScreen.name) {
                StartOrderScreen(
                    onStartOrderButtonClicked = {
                        navController.navigate(LunchTrayAppScreen.EntreeScreen.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            composable(route = LunchTrayAppScreen.EntreeScreen.name) {
                EntreeMenuScreen(
                    options = entreeMenuItems,
                    onCancelButtonClicked = { navigateToStart() },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayAppScreen.SideDishScreen.name)
                    },
                    onSelectionChanged = { viewModel.updateEntree(it) },
                )
            }
            composable(route = LunchTrayAppScreen.SideDishScreen.name) {
                SideDishMenuScreen(
                    options = sideDishMenuItems,
                    onCancelButtonClicked = { navigateToStart() },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayAppScreen.AccompanimentScreen.name)
                    },
                    onSelectionChanged = { viewModel.updateSideDish(it) }
                )
            }
            composable(route = LunchTrayAppScreen.AccompanimentScreen.name) {
                AccompanimentMenuScreen(
                    options = accompanimentMenuItems,
                    onCancelButtonClicked = { navigateToStart() },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayAppScreen.CheckoutScreen.name)
                    },
                    onSelectionChanged = { viewModel.updateAccompaniment(it) }
                )
            }
            composable(route = LunchTrayAppScreen.CheckoutScreen.name) {
                CheckoutScreen(
                    orderUiState = uiState,
                    onNextButtonClicked = { navigateToStart() },
                    onCancelButtonClicked = { navigateToStart() }
                )
            }
        }
    }
}



@Composable
@Preview
fun Test() {
    LunchTrayApp()
}
