package world.junseo.co.kr.lottomanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.coroutines.*
import world.junseo.co.kr.lottomanager.databinding.ActivityMainBinding
import world.junseo.co.kr.lottomanager.db.LottoDB
import world.junseo.co.kr.lottomanager.repository.LottoRepository

class MainActivity : AppCompatActivity() {
    private var lottoRepository: LottoRepository? = null
    private lateinit var binding: ActivityMainBinding

    var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.mainactivity = this@MainActivity
        setContentView(binding.root)

        lottoRepository = LottoRepository()
        lottoRepository?.dbWorker()

        CoroutineScope(Dispatchers.Main).launch {
            binding.tvLastNumber.text = NumberManager.getInstance()?.lastWinningNumber() ?: ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        job?.cancel()

        lottoRepository?.stopDBWorker()
        LottoDB.destroyInstance()
        NumberManager.destroyInstance()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }

    fun generateNumber() {
        job = CoroutineScope(Dispatchers.IO).launch  {
            val num1 = NumberManager.getInstance()?.generateNumber()
            val num2 = NumberManager.getInstance()?.generateNumber()
            val num3 = NumberManager.getInstance()?.randomBySpecialNumber()
            val num4 = NumberManager.getInstance()?.randomBySpecialNumber()
            val num5 = NumberManager.getInstance()?.randomByDate()

            withContext(Dispatchers.Main) {
                num1?.sorted()?.let {
                    val numList = "${it[0]}, ${it[1]}, ${it[2]}, ${it[3]}, ${it[4]}, ${it[5]} (자주 나오는 숫자 1)\n\n"
                    binding.tvWinningNumbers.text = numList
                }

                num2?.sorted()?.let {
                    val numList = "${it[0]}, ${it[1]}, ${it[2]}, ${it[3]}, ${it[4]}, ${it[5]} (자주 나오는 숫자 2)\n\n"
                    binding.tvWinningNumbers.append(numList)
                }

                num3?.sorted()?.let {
                    val numList = "${it[0]}, ${it[1]}, ${it[2]}, ${it[3]}, ${it[4]}, ${it[5]} (스페셜 번호 1)\n\n"
                    binding.tvWinningNumbers.append(numList)
                }

                num4?.sorted()?.let {
                    val numList = "${it[0]}, ${it[1]}, ${it[2]}, ${it[3]}, ${it[4]}, ${it[5]} (스페셜 번호 2)\n\n"
                    binding.tvWinningNumbers.append(numList)
                }

                num5?.sorted()?.let {
                    val numList = "${it[0]}, ${it[1]}, ${it[2]}, ${it[3]}, ${it[4]}, ${it[5]} (날짜)\n\n"
                    binding.tvWinningNumbers.append(numList)
                }
            }
        }
    }
}