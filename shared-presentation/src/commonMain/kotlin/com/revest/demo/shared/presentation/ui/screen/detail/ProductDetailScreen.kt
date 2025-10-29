package com.revest.demo.shared.presentation.ui.screen.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil3.compose.rememberAsyncImagePainter
import com.revest.demo.shared.domain.model.detail.ProductDetails
import com.revest.demo.shared.domain.model.detail.Review
import com.revest.demo.shared.presentation.core.ViewState
import com.revest.demo.shared.presentation.ui.screen.Screen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

internal data object ProductDetailScreen : Screen<Long>() {

    override val path: String = "product/{productId}"

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument("productId") { type = NavType.LongType }
    )

    override fun route(params: Long): String = "product/$params"

    @OptIn(KoinExperimentalAPI::class)
    override fun NavGraphBuilder.composable(navController: NavController) {
        composable(route = path, arguments = arguments) {
            val viewModel: ProductDetailViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            when (val detailsState = uiState.productDetails) {
                is ViewState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ViewState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(detailsState.error?.message ?: "An error occurred.")
                    }
                }
                is ViewState.Success -> {
                    ProductDetailContent(
                        product = detailsState.data,
                        onBackClick = { navController.popBackStack() },
                        onShareClick = { /*TODO*/ }
                    )
                }
            }
        }
    }
}

// --- Screen ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ProductDetailContent(product: ProductDetails, onBackClick: () -> Unit, onShareClick: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(product.title, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onShareClick) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it).fillMaxSize()) {
            item { ProductImagePager(images = product.images) }
            item { ProductHeader(product = product, modifier = Modifier.padding(16.dp)) }
            item { ProductTags(tags = product.tags, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) }
            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }
            item { ProductInfoSection("Details", product, modifier = Modifier.padding(16.dp)) }
            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }
            item { ProductDescription(description = product.description, modifier = Modifier.padding(16.dp)) }
            item { Divider(modifier = Modifier.padding(vertical = 8.dp)) }
            item { ProductReviews(reviews = product.reviews, modifier = Modifier.padding(16.dp)) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { Button(onClick = {}, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) { Text("Add to Cart") } }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// --- UI Components ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProductImagePager(images: List<String>) {
    val pagerState = rememberPagerState { images.size }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().height(300.dp),
        ) { page ->
            val painter = rememberAsyncImagePainter(model = images[page])
            Image(
                painter = painter,
                contentDescription = "Product image ${page + 1}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            images.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary else Color.LightGray)
                )
            }
        }
    }
}

@Composable
private fun ProductHeader(product: ProductDetails, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(product.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107))
            Text(" ${product.rating} (${product.reviews.size} reviews)", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("$${product.price}", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ProductTags(tags: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text("Tags", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(tags) { tag ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductInfoSection(title: String, product: ProductDetails, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow("SKU", product.sku)
        InfoRow("Weight", "${product.weight} kg")
        InfoRow("Dimensions", "${product.dimensions.width} x ${product.dimensions.height} x ${product.dimensions.depth} cm")
        InfoRow("Warranty", product.warrantyInformation)
        InfoRow("Shipping", product.shippingInformation)
        InfoRow("Availability", product.availabilityStatus)
        InfoRow("Return Policy", product.returnPolicy)
        InfoRow("Min Order", "${product.minimumOrderQuantity}")
    }
}

@Composable
private fun InfoRow(label: String, value: String, icon: ImageVector = Icons.Default.Info) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.size(8.dp))
        Text("$label: $value", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ProductDescription(description: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Description", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(description, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ProductReviews(reviews: List<Review>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Reviews", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        reviews.forEachIndexed { index, review ->
            ReviewItem(review = review)
            if (index < reviews.lastIndex) {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
private fun ReviewItem(review: Review, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(review.reviewerName, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Row {
                (1..5).forEach { star ->
                    Icon(
                        Icons.Default.Star, contentDescription = null,
                        tint = if (star <= review.rating) Color(0xFFFFC107) else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(review.comment, style = MaterialTheme.typography.bodyMedium)
    }
}
