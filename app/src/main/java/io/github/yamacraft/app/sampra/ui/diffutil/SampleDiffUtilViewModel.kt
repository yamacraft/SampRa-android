package io.github.yamacraft.app.sampra.ui.diffutil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SampleDiffUtilViewModel : ViewModel() {

    private val _progress = MutableLiveData<Boolean>().apply { value = false }
    val progress: LiveData<Boolean> = _progress

    private val _items = MutableLiveData<List<SampleDiffUtilListAdapter.SampleDillUtilItem>>()
    val items: LiveData<List<SampleDiffUtilListAdapter.SampleDillUtilItem>> = _items

    init {
        _progress.value = true
        _items.value = createItems()
        _progress.value = false
    }

    fun refresh() {
        _progress.value = true
        _items.value = createItems()
        _progress.value = false
    }

    fun itemCountUp(id: Int) {
        _items.value = _items.value?.map {
            if (it.id == id) {
                it.copy(count = it.count + 1)
            } else {
                it
            }
        }
    }

    private fun createItems(): List<SampleDiffUtilListAdapter.SampleDillUtilItem> {
        val items = mutableListOf<SampleDiffUtilListAdapter.SampleDillUtilItem>()
        for (i in 0..100) {
            items.add(SampleDiffUtilListAdapter.SampleDillUtilItem(i, 0))
        }
        return items.toList().shuffled()
    }
}
