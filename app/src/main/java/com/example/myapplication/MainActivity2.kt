package com.example.myapplication

import android.app.PendingIntent.getActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val button = findViewById<Button>(R.id.MainActivity2_button)
        button.setOnClickListener { toFirstActivity() }


        val buttonFragment = findViewById<Button>(R.id.MainActivity2_button2)
        val blankFragment = BlankFragment()
        buttonFragment.setOnClickListener { supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, blankFragment)
            commit()
        } }

        val buttonFragment2 = findViewById<Button>(R.id.MainActivity2_button3)
        val blankFragment2 = BlankFragment2()
        buttonFragment2.setOnClickListener { supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, blankFragment2)
            commit()
        } }

        val buttonRecycler = findViewById<Button>(R.id.buttonToRV)
        val recycleFragment = BlankFragment3()
        buttonRecycler.setOnClickListener{supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, recycleFragment)
            commit()
        }}
    }

    private fun toFirstActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}