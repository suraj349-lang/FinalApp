package com.example.finalapp.screens._1home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import com.example.finalapp.model.OfferModel
import com.example.finalapp.screens._1home._1_1Events.PersonalEventDesign2
import com.example.finalapp.screens._1home.utils.HomeError
import com.example.finalapp.screens.dialogBox.DialogLoading
import com.example.finalapp.utils.RequestState
import com.example.finalapp.viewmodels.EventsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivePosts(
    scrollBehavior: TopAppBarScrollBehavior,
    eventsViewModel: EventsViewModel,
    offersList: List<OfferModel>,
    padding: PaddingValues
) {
    when (val result = eventsViewModel.allOffers.value) {
        is RequestState.Success -> {
            eventsViewModel.offersList.value = result.data
            LazyColumn(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)){
                items(offersList) { offer ->
                    //ImageScreen(user)
                    // PostScreen(Post("", offer))
                    Log.d("offerData", "HomeScreenUI: $offer")
                    //VectorsListScreen(offer)
                    // PersonalEvent(offer)
                    PersonalEventDesign2(offer)

                }
            }

        }

        is RequestState.Error -> {
            HomeError(eventsViewModel)
            Toast.makeText(LocalContext.current, "${result.error.message}", Toast.LENGTH_SHORT).show()
        }

        RequestState.Loading -> {
            // HomeLoading(padding)
            DialogLoading()
        }

        RequestState.Idle -> {
            // HomeLoading(padding)
        }


    }

}

