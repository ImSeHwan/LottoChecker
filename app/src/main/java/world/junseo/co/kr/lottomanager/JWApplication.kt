package world.junseo.co.kr.lottomanager

import android.app.Application
import io.realm.Realm

class JWApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}