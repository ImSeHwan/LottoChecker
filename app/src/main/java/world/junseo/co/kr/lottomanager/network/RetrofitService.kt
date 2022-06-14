package world.junseo.co.kr.lottomanager.network

import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import world.junseo.co.kr.lottomanager.model.lottoItem
import java.util.concurrent.TimeUnit

interface RetrofitService {
    @GET("common.do")
    suspend fun getLottoItem(@Query("method") method: String, @Query("drwNo") drwNo: Int) : Response<lottoItem>

    companion object {
        var retrofitService: RetrofitService? = null

        fun getInstance() : RetrofitService? {
            if (retrofitService == null) {
                val okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://www.dhlottery.co.kr/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService
        }
    }
}