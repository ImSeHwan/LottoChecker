package world.junseo.co.kr.lottomanager.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import world.junseo.co.kr.lottomanager.model.goodNumberItem
import world.junseo.co.kr.lottomanager.model.lottoItem

@Dao
interface LottoNumDao {
    @Query("SELECT * FROM LottoInfo")
    fun getAll() : List<lottoItem>

    @Query("SELECT * FROM LottoInfo ORDER BY id DESC LIMIT 1")
    fun getLastItem() : lottoItem?

    @Insert(onConflict = REPLACE)
    fun insert(lottoItem: lottoItem)

    // 숫자가 존재하는지 확인
    @Query("SELECT COUNT(*) FROM LottoInfo where checkSum = :checkSum")
    fun isExistNum(checkSum: String) : Int

    // 그동안 가장 많이 나온 숫자 리스트
    @Query("SELECT lotto_num, SUM(cnt) AS cnt\n" +
            "  FROM ( SELECT drwtNo1 lotto_num, COUNT() cnt\n" +
            "           FROM LottoInfo\n" +
            "          GROUP BY drwtNo1\n" +
            "          UNION ALL\n" +
            "\t\t  \n" +
            "         SELECT drwtNo2 lotto_num, COUNT() cnt\n" +
            "           FROM LottoInfo\n" +
            "          GROUP BY drwtNo2\n" +
            "         UNION ALL\n" +
            "\t\t \n" +
            "         SELECT drwtNo3 lotto_num, COUNT() cnt\n" +
            "           FROM LottoInfo\n" +
            "          GROUP BY drwtNo3\n" +
            "         UNION ALL\n" +
            "\t\t \n" +
            "         SELECT drwtNo4 lotto_num, COUNT() cnt\n" +
            "           FROM LottoInfo\n" +
            "          GROUP BY drwtNo4\n" +
            "         UNION ALL\n" +
            "\t\t \n" +
            "         SELECT drwtNo5 lotto_num, COUNT() cnt\n" +
            "           FROM LottoInfo\n" +
            "          GROUP BY drwtNo5\n" +
            "          UNION ALL\n" +
            "\t\t  \n" +
            "         SELECT drwtNo6 lotto_num, COUNT() cnt\n" +
            "           FROM LottoInfo\n" +
            "          GROUP BY drwtNo6\n" +
            "       )\n" +
            " GROUP BY lotto_num\n" +
            " ORDER BY 2 DESC;")
    fun getPopularNumber() : List<goodNumberItem>
}