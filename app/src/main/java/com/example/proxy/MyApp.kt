package com.example.proxy

import android.app.Application
import com.example.proxy.models.ProxyDBItem
import dagger.hilt.android.HiltAndroidApp
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

@HiltAndroidApp
class MyApp: Application() {
    companion object{
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(ProxyDBItem::class)
            )
        )
    }
}