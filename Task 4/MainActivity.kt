package com.example.zadanie1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private fun obliczPunkty(currentShotsNumber :Int) :Int{
        if(currentShotsNumber == 1)
            return 5
        if(currentShotsNumber <= 4)
            return 3
        if(currentShotsNumber <= 6)
            return 2
        return 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username=intent.getStringExtra("username").toString()
        val db=DataBase(this)
        val strzel= findViewById<Button>(R.id.strzel);
        val ranking= findViewById<Button>(R.id.main_rank);
        val new_game=findViewById<Button>(R.id.new_game)
        val numer= findViewById<EditText>(R.id.liczba);
        val punkty=findViewById<TextView>(R.id.Punkty)
        val strzaly=findViewById<TextView>(R.id.Strzały)
        var guess= Random.nextInt(0,20);
        var howManyShots=0
        var wynik=db.getScore(username)


        strzaly.text="$username strzeliłeś: $howManyShots razy!"
        punkty.text="Zdobyte punkty: $wynik"
        Toast.makeText(applicationContext, "Podaj liczbę z zakresu 0:20", Toast.LENGTH_LONG).show()
        strzel.setOnClickListener(){
            howManyShots++;
            if(howManyShots ==11){
                val builder=android.app.AlertDialog.Builder(this@MainActivity);
                builder.setTitle("Spróbuj jeszcze raz!")
                builder.setMessage("Przegrałeś! Prawidłowa liczba to $guess")
                val dialog: android.app.AlertDialog=builder.create()
                dialog.show()
                howManyShots=0
                guess= Random.nextInt(0,20);
                strzaly.text="$username strzeliłeś: $howManyShots razy!"
                punkty.text="Zdobyte punkty: $wynik"
            }
            else if(numer.text.toString()=="" || numer.text.toString()=="-"){
                Toast.makeText(applicationContext, "Wpisz liczbę!", Toast.LENGTH_LONG).show()
                howManyShots--
            }
            else if(Integer.parseInt(numer.text.toString())>20 || Integer.parseInt(numer.text.toString())<0){
                Toast.makeText(applicationContext, "Wybierz liczbę całkowitą pomiędzy 0 a 20", Toast.LENGTH_LONG).show()
                howManyShots--
            }
            else{
                if(Integer.parseInt(numer.text.toString())==guess){
                    val builder=android.app.AlertDialog.Builder(this@MainActivity);
                    builder.setTitle("Gratulacje! Brawo!")
                    builder.setMessage("To była prawidłowa liczba!")
                    val dialog: android.app.AlertDialog=builder.create()
                    dialog.show()
                    wynik+=obliczPunkty(howManyShots)
                    db.updateScore(username, wynik)
                    val intent = Intent(this, Ranking::class.java)
                    startActivity(intent)
                    howManyShots=0
                    guess= Random.nextInt(0,20);
                }
                else if(Integer.parseInt(numer.text.toString()) < guess) {
                    Toast.makeText(applicationContext, "Twoja liczba jest mniejsza", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(applicationContext, "Twoja liczba jest większa", Toast.LENGTH_SHORT).show()
                }
                strzaly.text="$username strzeliłeś: $howManyShots razy!"
                punkty.text="Zdobyte punkty: $wynik"
            }
        }
        new_game.setOnClickListener(){
            guess= Random.nextInt(0,20);
            howManyShots=0
            wynik=db.getScore(username)
            strzaly.text="$username strzeliłeś: $howManyShots razy!"
            punkty.text="Zdobyte punkty: $wynik"
        }
        ranking.setOnClickListener(){
            val intent = Intent(this, Ranking::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
