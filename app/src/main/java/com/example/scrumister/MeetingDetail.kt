package com.example.scrumister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

@Suppress("DEPRECATION")
class MeetingDetail : AppCompatActivity() {
    var dailyScrum:DailyScrum?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_detail)
        var myObject = intent.getSerializableExtra ("scrum") as DailyScrum?
        dailyScrum=myObject
        setContent()
    }

    private fun setContent() {
        var date=findViewById<TextView>(R.id.textView7)
        date.text= dailyScrum!!.history!![1]!!.dateTime.toString()

        var attendees=findViewById<TextView>(R.id.textView)
        attendees.text= dailyScrum!!.attendees!![0].toString()+" and "+ dailyScrum!!.attendees!![1].toString()

        var transcript=findViewById<TextView>(R.id.textView4)
        transcript.text= dailyScrum!!.history!![1]!!.text

    }
}