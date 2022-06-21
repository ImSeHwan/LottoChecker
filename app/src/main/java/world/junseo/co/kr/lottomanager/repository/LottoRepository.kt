package world.junseo.co.kr.lottomanager.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import world.junseo.co.kr.lottomanager.NumberManager
import world.junseo.co.kr.lottomanager.db.LottoDB
import world.junseo.co.kr.lottomanager.model.lottoItem
import world.junseo.co.kr.lottomanager.network.RetrofitService
import java.text.SimpleDateFormat
import java.util.*

class LottoRepository() {
    var mJob: Job? = null

    var loop = true

    private fun getLottoItem(drwNo: Int) : Flow<lottoItem> = flow {
        var startIndex = drwNo

        try {
            while (loop) {
                val response = RetrofitService.getInstance()?.getLottoItem("getLottoNumber", startIndex++)
                response?.body()?.let {
                    if(it.returnValue == "success") {
                        emit(it)
                        delay(500)
                    } else {
                        loop = false
                    }
                } ?: run { loop = false }
            }
        } catch (e: Exception) {
            Log.e("sehwan", "번호가져오기 실패 : $e")
            loop = false
        }
    }

    fun dbWorker() {
        mJob = CoroutineScope(IO).launch {
            val lastItem = LottoDB.getInstance()?.lottoNumDao()?.getLastItem()
            lastItem?.let {
                Log.d("sehwan", "id(${lastItem.id}), date(${lastItem.drwNoDate})")

                val todayDate = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
                val lastDate = dateFormat.parse(lastItem.drwNoDate)?.time
                lastDate?.let {
                    val fewDay = (todayDate - lastDate) / (24*60*60*1000)
                    Log.d("sehwan", "${fewDay}일 전 데이터, 날짜정보(${lastItem.drwNoDate})")
                    if(fewDay > 7) {
                        getLottoItem(lastItem.id).collect { item ->
                            item.apply {
                                val nums = intArrayOf(drwtNo1, drwtNo2, drwtNo3, drwtNo4, drwtNo5, drwtNo6)
                                NumberManager.getInstance()?.let {
                                    checkSum = NumberManager.getInstance()?.checkSumString(nums) ?: ""
                                }
                            }
                            LottoDB.getInstance()?.lottoNumDao()?.insert(item)
                        }
                    } else {
                        Log.d("sehwan", "수집할 데이터가 없다!!!")
                    }
                }
            } ?: run {
                Log.d("sehwan", "데이터가 없다 처음부터 넣어라!!!")
                getLottoItem(1).collect { item ->
                    item.apply {
                        val nums = intArrayOf(drwtNo1, drwtNo2, drwtNo3, drwtNo4, drwtNo5, drwtNo6)
                        NumberManager.getInstance()?.let {
                            checkSum = NumberManager.getInstance()?.checkSumString(nums) ?: ""
                        }
                    }
                    LottoDB.getInstance()?.lottoNumDao()?.insert(item)
                }
            }
        }
    }

    fun stopDBWorker() {
        if(loop) loop = false

        CoroutineScope(Default).launch {
            delay(500)
            mJob?.let {
                if(mJob?.isActive == true) {
                    Log.d("sehwan", "stopDBWorker working")
                    mJob?.cancel()
                }
            }
        }
    }
}