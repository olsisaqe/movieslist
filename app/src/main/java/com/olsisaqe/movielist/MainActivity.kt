package com.olsisaqe.movielist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.mainFragmentContainer, MainFragment())
                .commit()
    }
}
