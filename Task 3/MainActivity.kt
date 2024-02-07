package com.example.zadanie1

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
    fun setRecord(score :Int){
        val sharedScore = this.getSharedPreferences("com.example.zadanie1.shared",0)
        val edit = sharedScore.edit()
        edit.putInt("score", score)
        edit.apply()
    }
    fun getRecord() :Int{
        val sharedScore = this.getSharedPreferences("com.example.zadanie1.shared",0)
        var score = sharedScore.getInt("score", 0)
        return score
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val strzel= findViewById<Button>(R.id.strzel);
        val new_game=findViewById<Button>(R.id.new_game)
        val reset=findViewById<Button>(R.id.reset)
        val numer= findViewById<EditText>(R.id.liczba);
        val punkty=findViewById<TextView>(R.id.Punkty)
        val strzaly=findViewById<TextView>(R.id.Strzały)
        var guess= Random.nextInt(0,20);
        var howManyShots=0
        var wynik=getRecord()
        strzaly.text="Strzeliłeś: $howManyShots razy!"
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
                wynik=0
                guess= Random.nextInt(0,20);
                strzaly.text="Strzeliłeś: $howManyShots razy!"
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
                    setRecord(wynik)
                    howManyShots=0
                    guess= Random.nextInt(0,20);
                }
                else if(Integer.parseInt(numer.text.toString()) < guess) {
                    Toast.makeText(applicationContext, "Twoja liczba jest mniejsza", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(applicationContext, "Twoja liczba jest większa", Toast.LENGTH_SHORT).show()
                }
                strzaly.text="Strzeliłeś: $howManyShots razy!"
                punkty.text="Zdobyte punkty: $wynik"
            }
        }
        new_game.setOnClickListener(){
            guess= Random.nextInt(0,20);
            howManyShots=0
            wynik=0
            strzaly.text="Strzeliłeś: $howManyShots razy!"
            punkty.text="Zdobyte punkty: $wynik"
        }
        reset.setOnClickListener(){
            wynik=0
            punkty.text="Zdobyte punkty: $wynik"
        }
    }
}
