package com.example.assignment2.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.assignment2.data.BoredActivity

class BoredActivityItemViewModel(val boredActivity: BoredActivity) {

    val showFavourite: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val showSent: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    val onClick: () -> Unit = {
        _favourite = !_favourite
    }

    val onSend: () -> Unit = {
        _isSent = !_isSent
    }

    private var _favourite: Boolean = false
        set(value) {
            field = value
            showFavourite.postValue(value)
        }

    private var _isSent: Boolean = false
        set(value) {
            field = value
            showSent.postValue(value)
        }

    init {
        _isSent = false
        _favourite = false
    }
}