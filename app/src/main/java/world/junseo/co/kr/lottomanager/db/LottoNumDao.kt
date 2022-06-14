package world.junseo.co.kr.lottomanager.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import world.junseo.co.kr.lottomanager.model.lottoItem

@Dao
interface LottoNumDao {
    @Query("SELECT * FROM LottoInfo")
    fun getAll() : List<lottoItem>

    @Query("SELECT * FROM LottoInfo ORDER BY id DESC LIMIT 1")
    fun getLastItem() : lottoItem

    @Insert(onConflict = REPLACE)
    fun insert(lottoItem: lottoItem)
}