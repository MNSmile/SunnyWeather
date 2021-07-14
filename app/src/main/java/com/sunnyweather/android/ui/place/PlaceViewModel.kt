package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place

class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>() // 用户对界面上显示的城市数据进行缓存，可以保证数据在屏幕发生旋转时不会丢失

    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query = query)
    }

    fun searchPlaces(query: String) { // 每当searchPlaces()方法被调用且观察到searchLiveData数据发生改变时，就会执行switchMap()方法，并返回一个可观察的LiveData
        searchLiveData.value = query
    }
}