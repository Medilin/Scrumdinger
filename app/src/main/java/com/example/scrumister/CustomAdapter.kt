package com.example.scrumister

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


class CustomAdapter(
    private val context: Context,
    private val dataset: ArrayList<DailyScrum>,

):
        RecyclerView.Adapter<CustomAdapter.ViewHolder>()
{
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val textView1:TextView
        val textView2:TextView
        val textView3:TextView
        val cardView:CardView


        init {
             textView1=view.findViewById(R.id.textView3)
             textView2=view.findViewById(R.id.textView4)
             textView3=view.findViewById(R.id.textView5)
             cardView=view.findViewById(R.id.cardView1)
        }


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_view,viewGroup,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.textView1.text = dataset[position].title
        viewHolder.textView2.text = dataset[position].attendees?.count().toString()
        viewHolder.textView3.text = dataset[position].lengthInMinutes.toString()
        dataset[position].theme?.let { viewHolder.cardView.setCardBackgroundColor(it) }
        val item = dataset[position]
       // val key=keys[position]
        //Addition



        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context, DetailScrumActivity::class.java)
            intent.putExtra("scrum", item)
           // intent.putExtra("key",)
            context.startActivity(intent)
        }




        }
    }


