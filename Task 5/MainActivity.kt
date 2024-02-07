package com.example.jsonapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val callone = serviceGenerator.getTodos()

        callone.enqueue(object : Callback<MutableList<Todos>>
        {
            override fun onResponse(
                call: Call<MutableList<Todos>>,
                response: Response<MutableList<Todos>>
            ) {
                if(response.isSuccessful){
                    takePosts(response.body() as ArrayList<Todos>);

                }
            }

            override fun onFailure(call: Call<MutableList<Todos>>, t: Throwable){
                t.printStackTrace()
            }
        });
    }

    fun takePosts(todos: ArrayList<Todos>){
        val calltwo= serviceGenerator.getPosts()
        calltwo.enqueue(object : Callback<MutableList<Posts>>
        {
            override fun onResponse(
                call: Call<MutableList<Posts>>,
                response: Response<MutableList<Posts>>
            ) {
                if(response.isSuccessful){
                    showUsers(todos,response.body() as ArrayList<Posts>);
                }
            }

            override fun onFailure(call: Call<MutableList<Posts>>, t: Throwable){
                t.printStackTrace()
            }
        });
    }
    fun showUsers(todos: ArrayList<Todos>, posts : ArrayList<Posts>){
        val userlist=findViewById<ListView>(R.id.userslist)
        val callthree=serviceGenerator.getUsers()
        callthree.enqueue(object : Callback<MutableList<Users>>{
            override fun onResponse(
                call: Call<MutableList<Users>>,
                response: Response<MutableList<Users>>
            ) {
                if(response.isSuccessful){
                    val users= ArrayList<String>() //to remember if we use () we can use function add
                    for(usr in response.body()!!){
                        var numberofTasks=0
                        var numberofPosts=0
                        var completedTasks=0
                        for(todo in todos){
                            if(todo.userId==usr.id){
                                numberofTasks++;
                                if(todo.completed.toBoolean()){
                                    completedTasks++;
                                }
                            }
                        }
                        for(post in posts){
                            if(post.userId==usr.id){
                                numberofPosts++;
                            }
                        }
                        val result="\n${usr.name} \nEmail: ${usr.email} \nZadania: $completedTasks/$numberofTasks \nLiczba postów: $numberofPosts \n"
                        users.add(result)
                    }
                    userlist.adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, users)
                    userlist.setOnItemClickListener() {_, _, position, _ ->
                        val intent= Intent(this@MainActivity, userinformation::class.java);
                        intent.putExtra("Id", position+1) //get the userid
                        startActivity(intent)
                    }
                }
            }
            override fun onFailure(call: Call<MutableList<Users>>, t: Throwable){
                t.printStackTrace()
            }
        });
    }
}