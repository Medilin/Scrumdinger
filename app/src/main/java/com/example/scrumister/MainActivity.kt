package com.example.scrumister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val data=ArrayList<DailyScrum>();
    var database: DatabaseReference=Firebase.database.reference;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        readAllScrums()

    }

    fun readAllScrums()
    {
        val myScrums = database.child("scrum-list")

        myScrums.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val keys=ArrayList<String>();
                val itemList = ArrayList<DailyScrum>()
                for (itemSnapshot in dataSnapshot.children) {
                    val item = itemSnapshot.getValue(DailyScrum::class.java)
                    itemList.add(item!!)
                    keys.add(itemSnapshot.key!!)
                }
                populateRecView(itemList,)
                println(itemList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors that occur while retrieving data from the database
                println("Error retrieving data from database: ${databaseError.message}")
            }
        })



    }




    fun populateRecView(itemList: ArrayList<DailyScrum>)
    {
       val recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
        // this creates a vertical layout Manager
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        val adapter = CustomAdapter(this,itemList)
        // Setting the Adapter with the recyclerview
        recyclerView.adapter = adapter

    }

    fun addScrum(view: View) {
        val intent = Intent(this, AddScrumActivity::class.java)
        startActivity(intent)
    }

}