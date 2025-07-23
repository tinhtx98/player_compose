package com.tinhtx.player.queue

import com.tinhtx.player.model.MediaItem
import com.tinhtx.player.domain.model.RepeatMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackQueue @Inject constructor() {

    private val _queue = MutableStateFlow<List<MediaItem>>(emptyList())
    val queue: StateFlow<List<MediaItem>> = _queue.asStateFlow()

    private val _currentIndex = MutableStateFlow(-1)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _shuffleMode = MutableStateFlow(false)
    val shuffleMode: StateFlow<Boolean> = _shuffleMode.asStateFlow()

    private val _repeatMode = MutableStateFlow(RepeatMode.OFF)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()

    private var originalQueue: List<MediaItem> = emptyList()

    fun setQueue(items: List<MediaItem>, startIndex: Int = 0) {
        originalQueue = items
        _queue.value = if (_shuffleMode.value) items.shuffled() else items
        _currentIndex.value = if (startIndex < items.size) startIndex else 0
    }

    fun addToQueue(item: MediaItem) {
        val currentQueue = _queue.value.toMutableList()
        currentQueue.add(item)
        _queue.value = currentQueue
    }

    fun removeFromQueue(index: Int) {
        if (index >= 0 && index < _queue.value.size) {
            val currentQueue = _queue.value.toMutableList()
            currentQueue.removeAt(index)
            _queue.value = currentQueue

            // Adjust current index if necessary
            if (index < _currentIndex.value) {
                _currentIndex.value = _currentIndex.value - 1
            } else if (index == _currentIndex.value && _currentIndex.value >= currentQueue.size) {
                _currentIndex.value = 0
            }
        }
    }

    fun getCurrentItem(): MediaItem? {
        val current = _currentIndex.value
        return if (current >= 0 && current < _queue.value.size) {
            _queue.value[current]
        } else null
    }

    fun getNextItem(): MediaItem? {
        val nextIndex = getNextIndex()
        return if (nextIndex >= 0 && nextIndex < _queue.value.size) {
            _queue.value[nextIndex]
        } else null
    }

    fun getPreviousItem(): MediaItem? {
        val prevIndex = getPreviousIndex()
        return if (prevIndex >= 0 && prevIndex < _queue.value.size) {
            _queue.value[prevIndex]
        } else null
    }

    fun skipToNext(): Boolean {
        val nextIndex = getNextIndex()
        return if (nextIndex != -1) {
            _currentIndex.value = nextIndex
            true
        } else false
    }

    fun skipToPrevious(): Boolean {
        val prevIndex = getPreviousIndex()
        return if (prevIndex != -1) {
            _currentIndex.value = prevIndex
            true
        } else false
    }

    fun skipToIndex(index: Int) {
        if (index >= 0 && index < _queue.value.size) {
            _currentIndex.value = index
        }
    }

    fun toggleShuffle() {
        _shuffleMode.value = !_shuffleMode.value
        if (_shuffleMode.value) {
            val currentItem = getCurrentItem()
            _queue.value = originalQueue.shuffled()
            // Keep current item at current position
            currentItem?.let { item ->
                val newIndex = _queue.value.indexOf(item)
                if (newIndex != -1) {
                    _currentIndex.value = newIndex
                }
            }
        } else {
            val currentItem = getCurrentItem()
            _queue.value = originalQueue
            currentItem?.let { item ->
                val newIndex = _queue.value.indexOf(item)
                if (newIndex != -1) {
                    _currentIndex.value = newIndex
                }
            }
        }
    }

    fun setRepeatMode(mode: RepeatMode) {
        _repeatMode.value = mode
    }

    fun clear() {
        _queue.value = emptyList()
        originalQueue = emptyList()
        _currentIndex.value = -1
    }

    private fun getNextIndex(): Int {
        val currentQueue = _queue.value
        val current = _currentIndex.value

        return when (_repeatMode.value) {
            RepeatMode.ONE -> current
            RepeatMode.ALL -> {
                if (current + 1 < currentQueue.size) {
                    current + 1
                } else {
                    0 // Loop back to start
                }
            }
            RepeatMode.OFF -> {
                if (current + 1 < currentQueue.size) {
                    current + 1
                } else {
                    -1 // End of queue
                }
            }
        }
    }

    private fun getPreviousIndex(): Int {
        val current = _currentIndex.value

        return when (_repeatMode.value) {
            RepeatMode.ONE -> current
            RepeatMode.ALL -> {
                if (current - 1 >= 0) {
                    current - 1
                } else {
                    _queue.value.size - 1 // Loop to end
                }
            }
            RepeatMode.OFF -> {
                if (current - 1 >= 0) {
                    current - 1
                } else {
                    -1 // Beginning of queue
                }
            }
        }
    }
}
