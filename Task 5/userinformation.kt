package com.example.jsonapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class userinformation : AppCompatActivity() {
    private val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userinformation)

        val id=intent.getIntExtra("Id", 0)
        val zadania=findViewById<ListView>(R.id.todos);
        val callone=serviceGenerator.getTodos()
        callone.enqueue(object : Callback<MutableList<Todos>>
        {
            override fun onResponse(
                call: Call<MutableList<Todos>>,
                response: Response<MutableList<Todos>>
            ) {
                if(response.isSuccessful){
                    val todos=ArrayList<String>()
                    for(todo in response.body()!!){
                        if(todo.userId==id){
                            if(todo.completed.toBoolean()){
                                todos.add("\nTytył zadania: \n${todo.title} \nWykonano!\n");
                            }
                            else {
                                todos.add("\nTytył zadania: \n${todo.title} \nDo roboty!\n");
                            }
                        }
                    }
                    zadania.adapter = ArrayAdapter(this@userinformation, android.R.layout.simple_list_item_1, todos)
                }
            }

            override fun onFailure(call: Call<MutableList<Todos>>, t: Throwable){
                t.printStackTrace()
            }
        });

        val posty=findViewById<ListView>(R.id.posts)
        val calltwo=serviceGenerator.getPosts()
        calltwo.enqueue(object : Callback<MutableList<Posts>>
        {
            override fun onResponse(
                call: Call<MutableList<Posts>>,
                response: Response<MutableList<Posts>>
            ) {
                if(response.isSuccessful){
                    val idks=ArrayList<Int>()
                    val posts=ArrayList<String>()
                    for(post in response.body()!!){
                        if(post.userId==id){
                            posts.add("\nTytuł posta: \n${post.title} \n \nOpis: \n${post.body}\n");
                            idks.add((post.id).toString().toInt())
                        }
                    }
                    posty.adapter = ArrayAdapter(this@userinformation, android.R.layout.simple_list_item_1, posts)

                posty.setOnItemClickListener(){_, _, position, _ ->
                    val intent= Intent(this@userinformation, comments::class.java);
                    intent.putExtra("Id_posta", idks[position]) //get the userid
                    startActivity(intent)
                }
                }
            }

            override fun onFailure(call: Call<MutableList<Posts>>, t: Throwable){
                t.printStackTrace()
            }
        });

    }
}