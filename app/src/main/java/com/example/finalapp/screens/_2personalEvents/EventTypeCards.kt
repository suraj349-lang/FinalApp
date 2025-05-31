package com.example.finalapp.screens._2personalEvents


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.finalapp.ui.theme.floatingActionBtnColor
import com.example.finalapp.utils.constants.Constants

@Preview(showBackground = true)
@Composable
fun EventTypeCards() {
    Surface(modifier = Modifier.fillMaxSize()) {
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2), modifier = Modifier.padding(10.dp)) {
            items(categories) { category ->
                EventTypeCardsItem(category)

            }
        }
    }
}

@Composable
fun EventTypeCardsItem(category: Category) {
    Card(onClick = { /*TODO*/ }, modifier = Modifier
        .padding(6.dp)
        .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = category.color)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = category.name, color = Color.White, fontFamily = Constants.FONT_MEDIUM)
        }

    }

}

data class Category(
    val name: String,
    val color: Color= floatingActionBtnColor,
    var isSelected: Boolean = false
)
val categories = listOf(
    Category("All", color = floatingActionBtnColor),
    Category("Sports", color = Color(0xFF07498A)),
    Category("Cinema", color = Color(0xFFCE9506)),
    Category("Politics", color = Color(0xFF06880C)),
    Category("Entertainment",color= Color(0xFFD32F2F)),
    Category("News",color= Color(0xFFC2185B)),
    Category("Comedy",color= Color(0xFF512DA8)),
    Category("Crime",color= Color(0xFF1D9BA8)),
    Category("Elections",color= Color(0xFF15AD9C)),
    Category("Protest",color= Color(0xFFAFB42B)),
)