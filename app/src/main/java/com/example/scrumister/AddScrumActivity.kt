package com.example.scrumister

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.String as String

class AddScrumActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_add_scrum)
        colorSpinner = findViewById(R.id.colorSpinner)

        val colorNames = colorMap.keys.toList()


        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colorNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colorSpinner.adapter = adapter
        database = FirebaseDatabase.getInstance().reference.child("scrum-list")
        getInputDetails()
    }
    fun writeNewScrum(
        id: Int,
        title: String,
        names: ArrayList<String>,
        duration: Int,
        theme: Int,
        list2: ArrayList<Meeting>
    )
    {
        val newObjectReference: DatabaseReference = database.push()
        val dailyScrum=DailyScrum(newObjectReference.key,title,names,duration,theme,list2)

        // Set the value of the new object at the generated reference
        newObjectReference.setValue(dailyScrum)
            .addOnSuccessListener {
                // Object added successfully
                val newObjectId: String = newObjectReference.key ?: ""
                // Use newObjectId or perform additional operations
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occurred while adding the object
            }




    }
    fun getInputDetails()
    {
        //Get Scrum Title
        val editText1=findViewById<EditText>(R.id.scrumTitle)
        val title=editText1.text

        //Get Scrum Color theme using spinner(drop-down menu)
        var selectedColorValue:Int=0
        colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedColorName = parent.getItemAtPosition(position).toString()
                selectedColorValue = colorMap[selectedColorName]!!
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        //Get Scrum Duration using seekbar progress
        val seekBar = findViewById<SeekBar>(R.id.seekbar)
        var duration=0
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // This method will be called when the user interacts with the SeekBar.
                // The 'progress' parameter is the current value of the SeekBar.
                val chosenValue = progress
                val textView1=findViewById<TextView>(R.id.meetingMinutes)
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
        val attendee1=editText3.text
        val editText4=findViewById<EditText>(R.id.attendee2)
        val attendee2=editText4.text
        var list1= ArrayList<String>()

        //Change 3
        var list2= ArrayList<Meeting>()
        var meeting=Meeting("TestDate","TestSummary")
        list2.add(meeting)


        //Finally locate the submit button
        val myButton = findViewById<Button>(R.id.button)

        myButton.setOnClickListener {
            list1.add(attendee1.toString())
            list1.add(attendee2.toString())
            //Ignore ID parameter
            writeNewScrum(3,title.toString(),list1,duration,selectedColorValue,list2 )
            //for logging
            Toast.makeText(this," Scrum Added", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }


    }
}
