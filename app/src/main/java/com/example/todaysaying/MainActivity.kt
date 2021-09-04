package com.example.todaysaying

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initData()
    }

    private fun initViews() {

    }

    private fun initData(){
        //시간 단축시키기
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 0
                }
        )

        //fetch자체가 서버와 통신이기에 비동기로 이루어짐. -> 리스너를 등록
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if(it.isSuccessful){//작업이 성공했다면
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))//기본 Api를 사용해서 Json파싱
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")

                displayQuotesPager(quotes, isNameRevealed)

            }
        }
    }

    private fun parseQuotesJson(json : String): List<Quote>{
        val jsonArray = JSONArray(json) //json String을 가지고 새로운 array를 만듬
        var jsonList =  emptyList<JSONObject>() //Json Array를 Jason object List로 바꿀 변수를 만듬 //하나씩 가져와서 Json List에 추가
        for(index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let{
                jsonList = jsonList + it //뒤에 하나씩 JsonObject가 붙음
            }
        }

        return jsonList.map{
            Quote(
                    quote = it.getString("quote"),
                    name = it.getString("name")
            )
        }
    }

    private fun displayQuotesPager(quotes :List<Quote> ,isNameRevealed : Boolean){
        viewPager.adapter = QuotesPagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameRevealed
        ) //mainActivity에 있는 ViewPager와 연동 -> 뷰페이저어댑터를 설정
    }


}