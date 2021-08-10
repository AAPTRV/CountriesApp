package com.example.task1new.app

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        AlertDialog.Builder(this).apply{
            setTitle("Confirmation")
            setMessage("Are you sure you want to exit?")
            setPositiveButton("Yes"){_, _ ->
                super.onBackPressed()
            }
            setNegativeButton("No"){_, _ ->
                Toast.makeText(this@MainActivity, "Thank you", Toast.LENGTH_LONG).show()
            }
            setCancelable(true)
        }.create().show()
    }
}