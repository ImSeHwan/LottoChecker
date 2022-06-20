package world.junseo.co.kr.lottomanager

import android.app.Application
import io.realm.Realm

class JWApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: JWApplication? = null

        fun getInstance() : JWApplication {
            return instance as JWApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}