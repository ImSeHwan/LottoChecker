package world.junseo.co.kr.lottomanager

import android.content.Context
import androidx.room.Room
import world.junseo.co.kr.lottomanager.db.LottoDB

class NumberManager {
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

    fun rand(): HashSet<Int> {
        val nums = HashSet<Int>()
        while (nums.size < 6) {
            nums.add(((Math.random() * 45) + 1).toInt())
        }

        return nums
    }
}