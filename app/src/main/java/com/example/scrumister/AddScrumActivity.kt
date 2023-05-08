package com.example.scrumister

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.String as String

class AddScrumActivity : AppCompatActivity() {
    var database: DatabaseReference = Firebase.database.reference;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_scrum)
        getInputDetails()
    }
    fun writeNewScrum(id:Int, title: String, attendees:ArrayList<String>, duration: Int, theme:Int)
    {
        val dailyScrum=DailyScrum(id,title,attendees,duration,theme)
        println(title)
        println(attendees[0])
        database.child("scrum-list").child(id.toString()).setValue(dailyScrum)

    }
    fun getInputDetails()
    {
        val editText1=findViewById<EditText>(R.id.scrumTitle)
        val title=editText1.text
        val seekBar = findViewById<SeekBar>(R.id.seekbar)
        var duration=0

        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // This method will be called when the user interacts with the SeekBar.
                // The 'progress' parameter is the current value of the SeekBar.
                val chosenValue = progress
                val textView1=findViewById<TextView>(R.id.meetingMinutes)
                textView1.text = progress.toString()+"minutes"
                duration=chosenValue
                // Do something with the chosen value
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // This method will be called when the user starts interacting with the SeekBar.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // This method will be called when the user stops interacting with the SeekBar.
            }
        })
        val editText2=findViewById<EditText>(R.id.scrumTheme)
        val color=editText2.text
        val editText3=findViewById<EditText>(R.id.attendee1)
        val attendee1=editText3.text
        val editText4=findViewById<EditText>(R.id.attendee2)
        val attendee2=editText4.text
        var list1= arrayListOf<kotlin.String>()
        list1.add(attendee1.toString())
        list1.add(attendee2.toString())
        val myButton = findViewById<Button>(R.id.button)

        myButton.setOnClickListener {
            writeNewScrum(3,title.toString(),list1,duration, Color.BLUE)
            println("Scrum Added")
            println(attendee1)
            println(attendee2)

        }


    }
}
