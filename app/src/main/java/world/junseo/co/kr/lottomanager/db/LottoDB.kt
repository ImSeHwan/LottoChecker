package world.junseo.co.kr.lottomanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import world.junseo.co.kr.lottomanager.model.lottoItem

@Database(entities = [lottoItem::class], version = 1, exportSchema = false)
abstract class LottoDB : RoomDatabase(){
    abstract fun lottoNumDao() : LottoNumDao

    companion object {
        private var INSTANCE : LottoDB? = null

        fun getInstance(context: Context) : LottoDB? {
            if (INSTANCE == null) {
                synchronized(LottoDB::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        LottoDB::class.java, "lotto.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}