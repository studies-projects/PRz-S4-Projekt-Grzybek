package com.example.grzybekapk.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.grzybekapk.R

class EventsAdapter(val eventsList:ArrayList<Event>) : RecyclerView.Adapter<EventsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v =LayoutInflater.from(p0?.context).inflate(R.layout.event_item_itemview,p0,false)
        return ViewHolder(v)
    }
    lateinit var mClickListener: ClickListener
    fun setOnItemClickListener(aClickListener: ClickListener){
        mClickListener=aClickListener
    }
    interface ClickListener{
        fun onClick(pos: Int, aView: View)
    }
    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val event : Event = eventsList[p1]
        p0?.textViewTitle.text = event.title
        p0?.textViewDate.text = event.date
        p0?.textViewDescription.text = event.description
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),View.OnClickListener{
        override fun onClick(v: View) {
            mClickListener.onClick(adapterPosition,v)
        }
        val textViewTitle = itemView.findViewById(R.id.titleView) as TextView
        val textViewDate = itemView.findViewById(R.id.dateView) as TextView
        val textViewDescription = itemView.findViewById(R.id.descriptionView) as TextView
        init{
            itemView.setOnClickListener(this)
        }
    }

}