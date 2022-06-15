package world.junseo.co.kr.lottomanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "LottoInfo")
class lottoItem(@PrimaryKey(autoGenerate = true) var id: Int,
                var returnValue: String,
                @ColumnInfo(name = "drwNoDate") var drwNoDate: String,
                @ColumnInfo(name = "drwNo") var drwNo: Int, //회차
                @ColumnInfo(name = "drwtNo1") var drwtNo1: Int,
                @ColumnInfo(name = "drwtNo2") var drwtNo2: Int,
                @ColumnInfo(name = "drwtNo3") var drwtNo3: Int,
                @ColumnInfo(name = "drwtNo4") var drwtNo4: Int,
                @ColumnInfo(name = "drwtNo5") var drwtNo5: Int,
                @ColumnInfo(name = "drwtNo6") var drwtNo6: Int,
                @ColumnInfo(name = "bnusNo") var bnusNo: Int //보너스
)

@Serializable
@Entity(tableName = "LottoInfo")
class goodNumberItem(
    @ColumnInfo(name = "lotto_num") var lottoNum: Int,
    @ColumnInfo(name = "cnt") var cnt: Int
)