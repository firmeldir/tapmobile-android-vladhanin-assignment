package com.vladhanin.tapmobiletestapp.domain

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

interface MainModel {

    sealed class State {
        data class Idle(val query: String, val elements: List<YoutubeEntity>) : State()
        data class Searching(val query: String) : State()
        data class Paging(val query: String, val elements: List<YoutubeEntity>) : State()
    }

    val state: Flow<State>

    fun searchVideos(query: String)
    fun fetchMoreVideos()
}

class DefaultMainModel(private val youtubeContract: YoutubeContract) : MainModel {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private sealed class Action {
        data class Search(val query: String) : Action()
        data class SearchResponse(val query: String, val result: List<YoutubeEntity>) : Action()
        object More : Action()
        data class MoreResponse(val result: List<YoutubeEntity>) : Action()
    }

    private val channel = Channel<Action>()

    private val _state: MutableStateFlow<MainModel.State> = MutableStateFlow(MainModel.State.Idle(""))
    override val state: Flow<MainModel.State>
        get() = merge(
            _state,
            flow {
                for (a in channel){
                    val old = _state.value
                    when (a) {
                        is Action.Search -> {
                            _state.value = MainModel.State.Searching(a.query)
                            sendSearchRequest(a.query)
                        }
                        is Action.SearchResponse -> {
                            when(old) {
                                is MainModel.State.Searching -> {
                                    if (old.query == a.query) {
                                        _state.value = MainModel.State.Idle(old.query, a.result)
                                    }
                                }
                                else -> {}
                            }
                        }
                        is Action.More -> {
                            when(old) {
                                is MainModel.State.Idle -> {
                                    _state.value = MainModel.State.Paging(old.query, old.elements)
                                    sendMoreRequest(old.query)
                                }
                                else -> {}
                            }
                        }
                        is Action.MoreResponse -> {
                            when(old) {
                                is MainModel.State.Paging -> {
                                    _state.value = MainModel.State.Idle(old.query, old.elements + a.result)
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        )

    override fun searchVideos(query: String) {
        TODO("Not yet implemented")
    }

    override fun fetchMoreVideos() {
        TODO("Not yet implemented")
    }

    private fun sendSearchRequest(query: String) {
        coroutineScope.launch {
            youtubeContract.search(query)
                .onSuccess {
                    channel.send(Action.SearchResponse(query, it))
                }.onFailure {
                    channel.send(Action.SearchResponse(query, emptyList()))
                }
        }
    }

    private fun sendMoreRequest(query: String) {
        coroutineScope.launch {
            youtubeContract.more(query)
                .onSuccess {
                    channel.send(Action.MoreResponse(it))
                }.onFailure {
                    channel.send(Action.MoreResponse(emptyList()))
                }
        }
    }
}