package com.example.zadanie1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username=findViewById<TextInputEditText>(R.id.username);
        val password=findViewById<TextInputEditText>(R.id.password);
        val db=DataBase(this)
        val ranking=findViewById<Button>(R.id.buttonrank);
        val zarejestruj=findViewById<Button>(R.id.signup)
        val zaloguj=findViewById<Button>(R.id.login)

        ranking.setOnClickListener(){
            val intent = Intent(this, Ranking::class.java)
            startActivity(intent)
        }

        zarejestruj.setOnClickListener(){
            if(username.text.toString()=="" || password.text.toString()==""){
                Toast.makeText(applicationContext, "Nazwa użytkownika i hasło nie mogą być puste!", Toast.LENGTH_SHORT).show()
            }
            else {
                val result = db.addUser(username.text.toString(), password.text.toString())

                if (Integer.parseInt(result.toString()) == 0) {
                    Toast.makeText(
                        applicationContext,
                        "Taki użytkownik już istnieje",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Pomyślnie zarejestrowano",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }

        zaloguj.setOnClickListener() {
            val result = db.checkUser(username.text.toString(), password.text.toString())
            if(result==0){
                Toast.makeText(
                    applicationContext,
                    "Nieprawidłowa nazwa użytkownika lub hasło",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("username", username.text.toString())
                startActivity(intent)
            }
        }
    }

}