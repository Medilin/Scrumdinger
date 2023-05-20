package com.example.scrumister

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


@Suppress("DEPRECATION")
class EditScrumActivity : AppCompatActivity() {
    var dailyScrum:DailyScrum?=null
    var newdailyScrum:DailyScrum?=null

    var database: DatabaseReference = Firebase.database.reference
    private lateinit var colorSpinner: Spinner
    private val colorMap: Map<String, Int> = mapOf(
        "Red" to Color.RED,
        "Green" to Color.GREEN,
        "White" to Color.WHITE,
        "Blue" to Color.BLUE,
        "Yellow" to Color.YELLOW
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_scrum)
        colorSpinner = findViewById(R.id.colorSpinner)

        val colorNames = colorMap.keys.toList()


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colorNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colorSpinner.adapter = adapter

        var myObject = intent.getSerializableExtra ("scrum") as DailyScrum?
        dailyScrum=myObject
        setContent()
    }


    fun updateScrum(
        id: String,
        title: String,
        names: ArrayList<String>,
        duration: Int,
        theme: Int,
        list2: ArrayList<Meeting>
    )
    {
        val scrumRef =database.child("scrum-list").child(id)
        val updates: MutableMap<String, Any> = HashMap()
        updates["title"] = title
        updates["attendees"] = names
        updates["duration"] = duration
        updates["theme"] = theme
        updates["history"]=list2
        scrumRef.updateChildren(updates)
            .addOnSuccessListener {
                // Update successful

            }
            .addOnFailureListener { e ->
                // Update failed
                // Handle any error conditions here
            }


    }







    fun setContent()
    {
        var pageTitle=findViewById<TextView>(R.id.textView7)
        pageTitle.text= dailyScrum!!.title.toString()

        val editText1=findViewById<EditText>(R.id.scrumTitle)
        editText1.setText(dailyScrum!!.title)
        val title= editText1.text

        //Get Scrum Color theme using spinner(drop-down menu)
        var selectedColorValue:Int=dailyScrum!!.theme!!
        var updatedValue:Int=0
        colorSpinner.setSelection(dailyScrum!!.theme!!)
        colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedColorName = parent.getItemAtPosition(position).toString()
                updatedValue = colorMap[selectedColorName]!!
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        //Get Scrum Duration using seekbar progress
        val seekBar = findViewById<SeekBar>(R.id.seekbar)
        var duration=0
        seekBar.progress = dailyScrum!!.lengthInMinutes!!
        var textView1=findViewById<TextView>(R.id.meetingMinutes)
        textView1.text=seekBar.progress.toString()+" minutes"

        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // This method will be called when the user interacts with the SeekBar.
                // The 'progress' parameter is the current value of the SeekBar.
                val chosenValue = progress
                textView1.text = progress.toString()+" minutes"
                duration=chosenValue
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // This method will be called when the user starts interacting with the SeekBar.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // This method will be called when the user stops interacting with the SeekBar.
            }
        })

        //Get Scrum Attendees
        val editText3=findViewById<EditText>(R.id.attendee1)
        editText3.setText(dailyScrum!!.attendees!![0])
        val attendee1=editText3.text
        val editText4=findViewById<EditText>(R.id.attendee2)
        editText4.setText(dailyScrum!!.attendees!![1])
        val attendee2=editText4.text
        var list1= ArrayList<String>()


        //Finally locate the Done button
        val myButton = findViewById<Button>(R.id.button)

        myButton.setOnClickListener {
            list1.add(attendee1.toString())
            list1.add(attendee2.toString())
            if(duration==0)
            {
                duration=seekBar.progress
            }
            if(selectedColorValue!=updatedValue)
            {
                selectedColorValue=updatedValue
            }
            var list2= ArrayList<Meeting>()
            var meeting=Meeting("22May2023","Test")
            list2.add(meeting)


            updateScrum(dailyScrum!!.id!!,title.toString(),list1,duration,selectedColorValue,list2 )
            //for logging
            Toast.makeText(this," Scrum updated!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }


    }


    }

