package world.junseo.co.kr.lottomanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import world.junseo.co.kr.lottomanager.db.LottoDB
import world.junseo.co.kr.lottomanager.repository.LottoRepository

class MainActivity : AppCompatActivity() {
    private var lottoRepository: LottoRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lottoRepository = LottoRepository(this)
        lottoRepository?.dbWorker()
    }

    override fun onDestroy() {
        super.onDestroy()

        lottoRepository?.stopDBWorker();
        LottoDB.destroyInstance()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }
}