package com.alihaidertest

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.alihaidertest.db.AppDatabase

class MainApplication : Application() {

    lateinit var myRoomDatabase: AppDatabase

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        myRoomDatabase = Room
            .databaseBuilder(applicationContext, AppDatabase::class.java, "my-room-database")
            .build()
    }

    companion object {
        lateinit var context: Context
    }
}