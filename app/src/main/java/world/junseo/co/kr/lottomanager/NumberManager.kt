package world.junseo.co.kr.lottomanager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import world.junseo.co.kr.lottomanager.db.LottoDB

class NumberManager() {

    companion object {
        private var INSTANCE : NumberManager? = null

        fun getInstance() : NumberManager? {
            if (INSTANCE == null) {
                synchronized(NumberManager::class) {
                    INSTANCE = NumberManager()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    // 중복없이 6자리 숫자를 뽑는다.
    fun rand(): HashSet<Int> {
        val nums = HashSet<Int>()
        while (nums.size < 6) {
            nums.add(((Math.random() * 45) + 1).toInt())
        }

        return nums
    }

    suspend fun lastWinningNumber(): String {
        var lastNumInfo = ""

        LottoDB.getInstance()?.let {
            val lottoItem = withContext(Dispatchers.IO) {
                it.lottoNumDao().getLastItem()
            }

            lottoItem?.let {
                lastNumInfo =
                    "당첨일 : ${lottoItem.drwNoDate}\n당첨번호 ${lottoItem.drwtNo1}, ${lottoItem.drwtNo2}, ${lottoItem.drwtNo3}, ${lottoItem.drwtNo4}, ${lottoItem.drwtNo5}," +
                            "${lottoItem.drwtNo6}, 뽀나스(${lottoItem.bnusNo})"
            }
        }

        return lastNumInfo
    }
}