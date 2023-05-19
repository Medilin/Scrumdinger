package com.example.scrumister

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class DetailScrumActivity : AppCompatActivity() {
    var dailyScrum:DailyScrum?=null
    private val colorMap: Map<Int, String> = mapOf(
        Color.GREEN to "Green",
        -65281 to "Pink",
        Color.RED to "Red",
        Color.WHITE to "White",
        Color.BLUE to "Blue"

    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_scrum)
        var myObject = intent.getSerializableExtra ("scrum") as DailyScrum?
        dailyScrum=myObject
        setContent()

    }
    fun setContent()
    {
        var title=findViewById<TextView>(R.id.detailTitle)
        title.text= dailyScrum!!.title.toString()
        var length=findViewById<TextView>(R.id.textView16)
        length.text= dailyScrum!!.lengthInMinutes.toString()+" minutes"
        var theme=findViewById<TextView>(R.id.textView18)
        theme.text= colorMap[dailyScrum!!.theme].toString()
        var attendee1=findViewById<TextView>(R.id.textView8)
        attendee1.text= dailyScrum!!.attendees!![0].toString()
        var attendee2=findViewById<TextView>(R.id.textView5)
        attendee2.text= dailyScrum!!.attendees!![1].toString()

    }
    fun editScrum(view: View)
    {
        val intent = Intent(this, EditScrumActivity::class.java)
        intent.putExtra("scrum", dailyScrum)
        startActivity(intent)

    }
    fun goBack(view:View)
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

}

