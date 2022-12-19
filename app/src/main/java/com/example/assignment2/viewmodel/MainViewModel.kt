package com.example.assignment2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment2.data.BoredActivity
import com.example.assignment2.network.IBoredClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    val activities: MutableLiveData<List<BoredActivityItemViewModel>> by lazy { MutableLiveData() }
    val loading: MutableLiveData<Boolean> by lazy { MutableLiveData() }

    private val retrofitClient = Retrofit.Builder().baseUrl("https://www.boredapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val boredClient = retrofitClient.create(IBoredClient::class.java)

    private lateinit var currentActivities: MutableList<BoredActivity>

    val getRandomActivities: (type: String) -> Unit = { type ->
        loading.postValue(true)
        currentActivities = emptyList<BoredActivity>().toMutableList()
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 1..15) {
                currentActivities.add(boredClient.getRandom(type.takeIf { it != ALL_CATEGORIES[0] }))
            }
            println(currentActivities)
            activities.postValue(currentActivities.map { BoredActivityItemViewModel(it) })
            loading.postValue(false)
        }
    }

    init {
        getRandomActivities.invoke(ALL_CATEGORIES[0])
    }

    companion object {
        val ALL_CATEGORIES = listOf("random", "education", "recreational", "social", "diy", "charity", "cooking", "relaxation", "music", "busywork")
    }
}