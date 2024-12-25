package com.example.mybook.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mybook.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen) // Подключаем splash_screen.xml

        // Переход на MainActivity через 2 секунды
        window.decorView.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Закрываем SplashActivity
        }, 2000)
    }
}
