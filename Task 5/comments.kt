package com.example.jsonapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class comments : AppCompatActivity() {
    private val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val id_posta=intent.getIntExtra("Id_posta", 0)
        val coms=findViewById<ListView>(R.id.coms)

        val callone=serviceGenerator.getComments()
        callone.enqueue(object : Callback<MutableList<Commentss>>
        {
            override fun onResponse(
                call: Call<MutableList<Commentss>>,
                response: Response<MutableList<Commentss>>
            ) {
                if(response.isSuccessful){
                    val comments=ArrayList<String>()
                    for(comm in response.body()!!){
                        if(comm.postId==id_posta){
                            comments.add("\nNazwa komentarza:\n${comm.name} \n \nTreść:\n${comm.body}\n")
                        }
                    }
                    coms.adapter = ArrayAdapter(this@comments, android.R.layout.simple_list_item_1, comments)
                }
            }

            override fun onFailure(call: Call<MutableList<Commentss>>, t: Throwable){
                t.printStackTrace()
            }
        });
        val button=findViewById<Button>(R.id.button)
        button.setOnClickListener(){
            val intent= Intent(this@comments, MainActivity::class.java);
            startActivity(intent)
        }
    }
}