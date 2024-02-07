package com.example.jsonapplication

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/users")
    fun getUsers(): Call<MutableList<Users>>

    @GET("/todos")
    fun getTodos(): Call<MutableList<Todos>>

    @GET("/posts")
    fun getPosts(): Call<MutableList<Posts>>

    @GET("/comments")
    fun getComments(): Call<MutableList<Commentss>>
}

