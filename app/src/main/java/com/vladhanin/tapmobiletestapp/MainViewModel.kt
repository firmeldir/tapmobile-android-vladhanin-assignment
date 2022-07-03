package com.vladhanin.tapmobiletestapp

import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModel
import com.vladhanin.tapmobiletestapp.domain.MainModel
import kotlinx.coroutines.flow.map

class MainViewModel (
    private val mainModel: MainModel = DI.provideMainModel()
) : ViewModel() {

    val uiState: Flow<List<ResultUiEntity>> = mainModel.state.map {
        emptyList()
    }
}