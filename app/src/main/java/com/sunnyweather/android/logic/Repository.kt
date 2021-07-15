package com.sunnyweather.android.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/*仓库层，在ViewModel和Model之间*/
object Repository {

    /*搜索城市数据，为了将异步获取的数据以响应式编程的方式通知给上一层，通常会返回一个LiveData对象，这里使用lifecycle-livedata-ktx库提供的功能，其自动构建并返回一个LiveData对象*/
    fun searchPlaces(query: String) = fire(Dispatchers.IO) { // Android不允许在主线程中进行网络请求，因此需要指定liveData()函数的线程类型为Dispatchers.IO
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query) // 调用了挂起函数
        if (placeResponse.status == "ok") {
            Log.d("hhh", "searchPlaces: ${placeResponse.places.toString()}")
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    /*刷新天气信息*/
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope { // 获取实时天气和未来天气信息是没有先后顺序的，因此让他们并行运算来提升程序的运行效率，但是要在同时得到它们的响应结果后才能进一步执行程序，这里使用协程async函数，由于async函数必须在协程作用域内才能调用，所以使用coroutineScope函数
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponce = deferredRealtime.await() // 获取 RealtimeResponce 对象
            val dailyResponse = deferredDaily.await() // 获取 DailyResponse 对象

            if (realtimeResponce.status == "ok" && dailyResponse.status == "ok") { // 如果status都OK，即获取数据成功
                val weather = Weather(realtimeResponce.result.realtime, dailyResponse.result.daily) // 把数据封装到 Weather中
                Result.success(weather) // 包装 Weather 对象
            } else {
                Result.failure( // 如果失败，则返回异常的状态
                    RuntimeException(
                        "realtime response status is ${realtimeResponce.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    /*封装网络请求的try-catch*/
    /**
     * 首先是调用 liveData() 方法，按照其标准定义来写的，传入context对象和block函数，返回LiveData<Result<T>>类型
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) = liveData<Result<T>>(context) {
        val result = try {
            block()
        } catch (e: Exception) {
            Result.failure<T>(e)
        }
        emit(result)
    }
}