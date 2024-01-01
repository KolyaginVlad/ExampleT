package com.example.examplet.ui.list

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.examplet.R
import com.example.examplet.ui.list.views.ListItem
import com.example.examplet.utils.base.subscribeEvents
import com.example.examplet.utils.base.subscribeScreenState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ListScreen(
    navigator: DestinationsNavigator,
    viewModel: ListViewModel = hiltViewModel(),
) {
    val state by viewModel.subscribeScreenState()
    val context = LocalContext.current
    viewModel.subscribeEvents {
        when (it) {
            is ListScreenEvent.ShowToast -> Toast.makeText(
                context, it.text, Toast.LENGTH_LONG
            ).show()

            is ListScreenEvent.GoBack -> navigator.navigateUp()
        }
    }
    ListScreenContent(
        state = state, onClickItem = viewModel::onClickItem, onBack = viewModel::onBack
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListScreenContent(
    state: ListScreenState,
    onClickItem: (Int) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.list_example)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    itemsIndexed(state.listOfItems) { index, itemData ->
                        ListItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = remember(index) { { onClickItem(index) } })
                                .animateItemPlacement(),
                            data = itemData
                        )
                    }
                }
            }
        }
    }
}