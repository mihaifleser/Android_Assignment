package com.example.assignment2.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.assignment2.data.BoredActivity

class BoredActivityItemViewModel(val boredActivity: BoredActivity, private val favourite: Boolean = false, private val isSent: Boolean = false) {

    val showFavourite: MutableLiveData<Boolean> by lazy { MutableLiveData() }
    val showSent: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    private var _favourite: Boolean = favourite
        set(value) {
            field = value
            showFavourite.postValue(value)
        }

    private var _isSent: Boolean = isSent
        set(value) {
            field = value
            showSent.postValue(value)
        }

}