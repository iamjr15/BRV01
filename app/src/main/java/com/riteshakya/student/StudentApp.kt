package com.riteshakya.student

import com.riteshakya.student.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class StudentApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
    companion object {
        var instance: StudentApp? = null
    }
    private var refWatcher: RefWatcher? = null

    fun mustDie(`object`: Any) {
        if (refWatcher != null) {
            refWatcher?.watch(`object`)
        }
    }
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
    }
}