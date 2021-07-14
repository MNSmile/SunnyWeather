package com.sunnyweather.android.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.SunnyWeatherApplication

class PlaceFragment : Fragment() {
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) } // 通过懒加载技术来获取 PlaceViewModel实例

    private lateinit var adapter : PlaceAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchPlaceEdit: EditText
    private lateinit var bgImageView: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place, container, false) // 加载 fragment_place 布局
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*初始化，实例对象*/
        recyclerView = activity?.findViewById(R.id.recyclerView)!!
        searchPlaceEdit = activity?.findViewById(R.id.searchPlaceEdit)!!
        bgImageView = activity?.findViewById(R.id.bgImageView)!!
        adapter = PlaceAdapter(this, viewModel.placeList)

        /*对RecyclerView进行配置 */
        val layoutManager = LinearLayoutManager(activity) // 配置layoutManager
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter                    // 构建adapter适配器
        /*搜索框内容设置*/
        searchPlaceEdit.addTextChangedListener { editable ->  //搜索框内容发生改变时，监听事件
            val content = editable.toString() // 获取搜索框内容
            if (content.isNotEmpty()){        // 如果搜索框内容不为空，传递给 PlaceViewModel的searchPlaces()方法，这样就可以发起网络请求
                viewModel.searchPlaces(content)
            } else {                          // 当搜索框内容为空时，将 RecyclerView 隐藏，并显示仅用于美观的图片，提示数据发生改变
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        /*搜索框结果显示设置*/
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null){  // 如果有城市天气信息，显示RecyclerView, 关闭背景图片，清除placeList列表中之前的信息，添加新的城市信息
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged() // 提示信息更新
            } else {              // 否则，提示没有查到任何地点，如果数据为空，则说明发生了异常，弹出出错信息
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}