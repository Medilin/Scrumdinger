package com.example.scrumister

import android.graphics.Color

data class DailyScrum(var id:Int?=null,var title: String?=null, var attendees:ArrayList<String>?=null, var lengthInMinutes: Int?=null,var theme:Int?=null) {

}