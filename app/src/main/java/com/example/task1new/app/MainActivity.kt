package com.example.task1new.app

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.example.task1new.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FragmentManager.OnBackStackChangedListener{
            Log.e(ContentValues.TAG, "MAIN ACTIVITY OnBackStackChangedListener: OnBackStackChangedListener: ${supportFragmentManager.backStackEntryCount}")
        }
    }

    override fun onBackPressed(){
        if(supportFragmentManager.backStackEntryCount == 0){
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}