package com.example.finalapp.screens._3createEvent.privateCreateEvent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import com.example.finalapp.screens._4profile.EventTopic


@Composable
fun CreateEventTypeScreen(
    event: String,
    onEventChange: (String) -> Unit,
    onNextClicked: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    // Filter the enum list based on user input
    val suggestions = remember(event) {
        if (event.isBlank()) emptyList()
        else EventTopic.values().filter {
            it.name.replace("_", " ", ignoreCase = true)
                .contains(event.trim(), ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add a Type", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = event,
            onValueChange = { onEventChange(it) },
            label = { Text("Search or type event type...") },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
        )

        // Show suggestions only if the field is focused and there are matches
        if (isFocused && suggestions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
            ) {
                items(suggestions) { topic ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = topic.name.replace("_", " ")
                                    .lowercase()
                                    .replaceFirstChar { it.uppercase() }
                            )
                        },
                        onClick = {
                            onEventChange(
                                topic.name.replace("_", " ")
                                    .lowercase()
                                    .replaceFirstChar { it.uppercase() }
                            )
                            isFocused = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = onNextClicked,
            enabled = event.isNotBlank()
        ) {
            Text(text = "Next")
        }
    }
}
