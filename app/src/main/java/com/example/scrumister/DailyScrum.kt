package com.example.scrumister

import android.graphics.Color
data class Meeting(val dateTime:String?=null,val text : String?=null):java.io.Serializable{

}

data class DailyScrum(var id:String?=null,var title: String?=null, var attendees:ArrayList<String>?=null, var lengthInMinutes: Int?=null,var theme:Int?=null,var history:ArrayList<Meeting>?=null) :java.io.Serializable{

}