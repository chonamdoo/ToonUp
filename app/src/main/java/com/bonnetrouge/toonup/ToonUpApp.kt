package com.bonnetrouge.toonup

import android.app.Application
import com.bonnetrouge.toonup.Commons.Ext.lazyAndroid
import com.bonnetrouge.toonup.DI.Components.DaggerToonUpAppComponent
import com.bonnetrouge.toonup.DI.Components.ToonUpAppComponent
import com.bonnetrouge.toonup.DI.Modules.ToonUpAppModule

class ToonUpApp : Application() {

    companion object {
        lateinit var app: ToonUpApp
            private set
    }

    val component: ToonUpAppComponent by lazyAndroid {
        DaggerToonUpAppComponent
                .builder()
                .toonUpAppModule(ToonUpAppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        component.inject(this)
    }
}