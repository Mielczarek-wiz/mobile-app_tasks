package com.example.zadanie1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class Ranking : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        val db = DataBase(this)

        val powrot=findViewById<Button>(R.id.powrot)
        powrot.setOnClickListener(){
            onBackPressed()
        }
        val lista = findViewById<ListView>(R.id.listarankingowa)

        val users: ArrayList<String> = db.showUser()

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, users)

        lista.adapter = arrayAdapter
    }
}