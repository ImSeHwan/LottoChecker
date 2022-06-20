package world.junseo.co.kr.lottomanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import world.junseo.co.kr.lottomanager.databinding.ActivityMainBinding
import world.junseo.co.kr.lottomanager.db.LottoDB
import world.junseo.co.kr.lottomanager.repository.LottoRepository

class MainActivity : AppCompatActivity() {
    private var lottoRepository: LottoRepository? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lottoRepository = LottoRepository()
        lottoRepository?.dbWorker()

        CoroutineScope(Dispatchers.Main).launch {
            binding.tvLastNumber.text = NumberManager.getInstance()?.lastWinningNumber() ?: ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        lottoRepository?.stopDBWorker();
        LottoDB.destroyInstance()
        NumberManager.destroyInstance()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }
}