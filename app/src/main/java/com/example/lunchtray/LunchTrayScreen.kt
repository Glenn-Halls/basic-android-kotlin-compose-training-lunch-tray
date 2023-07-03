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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.ui.OrderViewModel

enum class LunchTrayAppScreen(@StringRes val title: Int) {
    StartScreen(R.string.app_name),
    EntreeScreen(R.string.choose_entree),
    SideDishScreen(R.string.choose_side_dish),
    AccompanimentScreen(R.string.choose_accompaniment),
    CheckoutScreen(R.string.order_checkout)
}

// TODO: AppBar

@Composable
fun LunchTrayApp(
    navController: NavController = rememberNavController()
) {
    // Create Controller and initialization
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = LunchTrayAppScreen.valueOf(
        backStackEntry?.destination?.route ?: LunchTrayAppScreen.StartScreen.name
    )

    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = {
            // TODO: AppBar
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        // TODO: Navigation host
    }
}
