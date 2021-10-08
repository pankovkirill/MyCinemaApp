package com.example.mycinemaapp.app

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.room.Room
import com.example.mycinemaapp.model.dataBase.HistoryDao
import com.example.mycinemaapp.model.dataBase.HistoryDataBase
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.IllegalStateException

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("MyFMessagingService", "token = ${task.result.toString()}")
            }
        }
    }

    companion object {
        private var appInstance: App? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"

        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw
                        IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            HistoryDataBase::class.java,
                            DB_NAME
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return db!!.historyDao()
        }
    }
}