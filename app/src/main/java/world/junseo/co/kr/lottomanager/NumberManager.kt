package world.junseo.co.kr.lottomanager

import android.graphics.RadialGradient
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import world.junseo.co.kr.lottomanager.db.LottoDB
import kotlin.random.Random

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

    fun randomBySpecialNumber() : List<Int>{
        val currentTime = System.currentTimeMillis()
        val specialNums = mutableListOf(1*currentTime, 10*currentTime, 25*currentTime, 12*currentTime, 7*currentTime, 55*currentTime)
        specialNums.shuffle()

        val nums = HashSet<Int>()

        while (nums.size < 6) {
            nums.add(Random(specialNums[nums.size]).nextInt(45) + 1)
        }

        val checksumString = checkSumString(nums.toIntArray())
        val count = LottoDB.getInstance()?.lottoNumDao()?.isExistNum(checksumString) ?: 0

        if(count > 0) {
            randomBySpecialNumber()
        }

        return nums.toList()
    }

/*    fun rand(): HashSet<Int> {
        val nums = HashSet<Int>()

        while (nums.size < 6) {
            nums.add(((Math.random() * 45) + 1).toInt())
        }

        return nums
    }*/

    // 가중치에 따라 중복없이 랜덤으로 6자리 숫자를 뽑는다.
    fun randWeighted(): HashSet<Int> {
        val nums = HashSet<Int>()
        val random = Random
        while (nums.size < 6) {
            getWeightedRandom(makeNumList(), random)?.let { nums.add(it) }
        }

        return nums
    }

    fun generateNumber() : List<Int>? {
        var result = ""
        val numList = getInstance()?.randWeighted()
        numList?.let {
            result = checkSumString(it.toIntArray())
            Log.d("sehwan", "result : $result")

            val count = LottoDB.getInstance()?.lottoNumDao()?.isExistNum(result)
            if (count != null) {
                if(count > 0) {
                    generateNumber()
                }
            }
        }

        return numList?.toList()
    }

    fun checkSumString(nums:IntArray) : String{
        var result = ""
        nums.sort()

        for((x, i) in nums.withIndex()) {
            result += i.toString()
            if(x >= 6) {
                break
            }
        }

        return result
    }

    private fun makeNumList() : Map<Int, Double> {
        val map = HashMap<Int, Double>()
        val numList = LottoDB.getInstance()?.lottoNumDao()?.getPopularNumber()

        numList?.let {
            for (item in numList) {
                map[item.lottoNum] = item.cnt.toDouble()
            }
        }

        return map
    }

    //가중치를 통해서 랜덤한 수를 뽑는다.
    private fun <E> getWeightedRandom(weights: Map<E, Double>, random: Random): E? {
        var result: E? = null
        var bestValue = Double.MAX_VALUE
        for (element in weights.keys) {
            weights[element]?.let {
                val value = -Math.log(random.nextDouble()) / it
                if (value < bestValue) {
                    bestValue = value
                    result = element
                }
            }
        }
        return result
    }

    suspend fun lastWinningNumber(): String {
        var lastNumInfo = ""

        LottoDB.getInstance()?.let {
            val lottoItem = withContext(Dispatchers.IO) {
                it.lottoNumDao().getLastItem()
            }

            lottoItem?.let {
                lastNumInfo =
                    "당첨일 : ${lottoItem.drwNoDate}\n당첨번호 ${lottoItem.drwtNo1}, ${lottoItem.drwtNo2}, ${lottoItem.drwtNo3}, ${lottoItem.drwtNo4}, ${lottoItem.drwtNo5}, " +
                            "${lottoItem.drwtNo6}, 뽀나스(${lottoItem.bnusNo})"
            }
        }

        return lastNumInfo
    }
}