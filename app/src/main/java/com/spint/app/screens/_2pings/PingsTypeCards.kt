package com.spint.app.screens._2pings


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.spint.app.ui.theme.floatingActionBtnColor
import com.spint.app.utils.constants.Constants

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
    Category("All", color = floatingActionBtnColor), // Keep as is
    Category("Sports", color = Color(0xFFE53935)),       // Energetic Red for action and passion
    Category("Cinema", color = Color(0xFF5E35B1)),       // Deep Purple for creativity and drama
    Category("Politics", color = Color(0xFF1E88E5)),     // Calm, strong Blue for trust and structure
    Category("Entertainment", color = Color(0xFFFF7043)),// Vibrant Orange for excitement and engagement
    Category("News", color = Color(0xFF43A047)),         // Green for balance and freshness
    Category("Comedy", color = Color(0xFFFFC107)),       // Bright Yellow for cheerfulness and humor
    Category("Crime", color = Color(0xFF37474F)),        // Dark Slate for mystery and tension
    Category("Elections", color = Color(0xFF1976D2)),    // Rich Blue for authority and clarity
    Category("Protest", color = Color(0xFFD32F2F)),      // Bold Red for urgency and impact
)
