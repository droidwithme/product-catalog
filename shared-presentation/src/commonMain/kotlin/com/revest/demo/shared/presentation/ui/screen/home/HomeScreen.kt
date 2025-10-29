package com.revest.demo.shared.presentation.ui.screen.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.revest.demo.shared.presentation.core.ViewState
import com.revest.demo.shared.presentation.ui.screen.Screen
import com.revest.demo.shared.presentation.ui.screen.detail.ProductDetailScreen
import com.revest.demo.shared.presentation.ui.widgets.bar.ErrorBar
import com.revest.demo.shared.presentation.ui.widgets.bar.ErrorBarDefaults
import com.revest.demo.shared.presentation.ui.widgets.products.ProductsList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import revestdemo.shared_presentation.generated.resources.Res
import revestdemo.shared_presentation.generated.resources.app_name

internal data object HomeScreen : Screen<Unit>() {

    override val path: String = "main"

    override val arguments: List<NamedNavArgument> = emptyList()

    override fun route(params: Unit): String = path

    override fun NavGraphBuilder.composable(navController: NavController) {
        composable(route = path, arguments = arguments) {
            Home(
                onProductDetails = { productId ->
                    navController.navigate(ProductDetailScreen.route(productId))
                },
            )

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
internal fun Home(
    onProductDetails: (id: Long) -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
    snackbarDuration: SnackbarDuration = SnackbarDuration.Short,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()

    ErrorBar(
        error = uiState.error,
        duration = snackbarDuration,
        snackbarHostState = snackbarHostState,
        onDismissed = viewModel::onErrorConsumed,
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollBehavior,
                searchActive = uiState.searchActive,
                searchQuery = uiState.searchQuery,
                onSearchActiveChange = viewModel::onSearchActiveChange,
                onSearchQueryChange = viewModel::onSearchQueryChange,
            )
        },

        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .testTag(tag = ErrorBarDefaults.ErrorTestTag)
                    .navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        },
        contentWindowInsets = WindowInsets.ime
    ) { paddingValues ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            viewState = uiState,
            loadMore = viewModel::loadMore,
            onProductClick = { productId ->
                onProductDetails(productId)
            }
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    viewState: HomeViewState,
    loadMore: () -> Unit,
    onProductClick: (Long) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        if (viewState.searchActive) {
            when (val searchResultsState = viewState.searchResults) {
                is ViewState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is ViewState.Error -> {
                    Text(
                        text = searchResultsState.error?.message ?: "An error occurred",
                        modifier = Modifier.padding(16.dp).align(Alignment.Center)
                    )
                }

                is ViewState.Success -> {
                    val products = searchResultsState.data
                    if (products.isEmpty()) {
                        Text(
                            text = "No results found for '${viewState.searchQuery}'.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        ProductsList(
                            products = products,
                            loadMore = {},
                            onProductClick = onProductClick
                        )
                    }
                }
            }
        } else {
            when (val productsViewState = viewState.productsViewState) {
                is ViewState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is ViewState.Error -> {
                    Text(
                        text = productsViewState.error?.message ?: "An error occurred",
                        modifier = Modifier.padding(16.dp).align(Alignment.Center)
                    )
                }

                is ViewState.Success -> {
                    val products = productsViewState.data
                    if (products.isEmpty()) {
                        Text(
                            text = "No products found.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        ProductsList(
                            products = products,
                            loadMore = loadMore,
                            onProductClick = onProductClick
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    modifier: Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    searchActive: Boolean,
    searchQuery: String,
    onSearchActiveChange: (Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            AnimatedContent(
                targetState = searchActive,
                transitionSpec = {
                    if (targetState) {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> width } + fadeOut()
                    }
                }
            ) { isSearchActive ->
                if (isSearchActive) {
                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search products...") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            focusManager.clearFocus()
                        }),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = stringResource(resource = Res.string.app_name)
                    )
                }
            }
        },
        navigationIcon = {
            if (searchActive) {
                IconButton(onClick = { onSearchActiveChange(false) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (searchActive) {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search"
                        )
                    }
                }
            } else {
                IconButton(
                    modifier = Modifier.testTag(tag = HomeScreenDefaults.SearchIconTestTag),
                    onClick = { onSearchActiveChange(true) }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

internal object HomeScreenDefaults {

    const val SearchIconTestTag = "search_icon"

}