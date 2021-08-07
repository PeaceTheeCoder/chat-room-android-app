package com.theecoder.anonymus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private var msgs: List<MyDataItem>, private var myUsername:String):
RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val left_user: TextView? = itemView.findViewById<TextView>(R.id.left_name)
        val left_text: TextView? = itemView.findViewById<TextView>(R.id.left_text)
        val left_time: TextView? = itemView.findViewById<TextView>(R.id.left_time)

        val right_text: TextView? = itemView.findViewById<TextView>(R.id.right_text)
        val right_time: TextView? = itemView.findViewById<TextView>(R.id.right_time)

        val r_layout: RelativeLayout? = itemView.findViewById<RelativeLayout>(R.id.right_layout)
        val l_layout: RelativeLayout? = itemView.findViewById<RelativeLayout>(R.id.left_layout)
        init {

            itemView.setOnClickListener{
                val position: Int = adapterPosition
                Toast.makeText(itemView.context, "item # ${position+1}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(msgs[position].username == myUsername) {
            holder.right_text?.text = msgs[position].text
            holder.right_time?.text = msgs[position].date

            holder.l_layout?.visibility = View.GONE
            holder.r_layout?.visibility = View.VISIBLE
        }else{
            holder.left_user?.text = msgs[position].username
            holder.left_text?.text = msgs[position].text
            holder.left_time?.text = msgs[position].date

            holder.r_layout?.visibility = View.GONE
            holder.l_layout?.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return msgs.size
    }
}