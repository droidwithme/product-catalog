package com.revest.demo.shared.presentation.ui.widgets.products

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.revest.demo.shared.domain.model.products.Products

@Composable
public fun ProductsList(
    products: List<Products>,
    loadMore: () -> Unit,
    onProductClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(products, key = { _, product -> product.id }) { index, product ->
            ProductItem(product = product, onClick = { onProductClick(product.id) })
            Divider(modifier= Modifier.height(0.3.dp))
            if (index >= products.size - 4 && products.isNotEmpty()) {
                LaunchedEffect(products.size) {
                    loadMore()
                }
            }
        }
    }
}

internal object ProductsListDefaults {

    const val list: String = "product_list"


}