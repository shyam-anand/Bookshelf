package com.shyamanand.bookshelf

import android.app.Application
import com.shyamanand.bookshelf.data.AppContainer
import com.shyamanand.bookshelf.network.DefaultAppContainer

const val TAG = "BookShelfApplication"

class BookshelfApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}