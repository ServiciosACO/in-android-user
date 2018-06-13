package co.kubo.indiesco.activities

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by estacion on 13/06/18.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }

}