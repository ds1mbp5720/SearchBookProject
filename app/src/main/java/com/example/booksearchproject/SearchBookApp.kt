package com.example.booksearchproject

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SearchBookApp: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}