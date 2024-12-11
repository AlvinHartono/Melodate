package com.example.melodate.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.melodate.R
import com.example.melodate.data.model.Message

class MessageAdapter(val context: Context, private val messages: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val ITEM_RECEIVED = 2
        val ITEM_SENT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_RECEIVED) {
            // inflate received
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.received, parent, false)
            return ReceivedViewHolder(view)
        } else {
            // inflate sent
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messages[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            //do the stuff for sent view holder
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message


        } else {
            //do the stuff for received view holder
            val viewHolder = holder as ReceivedViewHolder
            holder.receivedMessage.text = currentMessage.message
            Glide.with(context).load(currentMessage.profileUrl)
                .placeholder(R.drawable.baseline_person_24).circleCrop().into(holder.profileImage)


        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messages[position]
        val senderId = currentMessage.senderId
        val currentUserId = currentMessage.currentUserId
        return if (senderId == currentUserId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVED
        }
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.tv_sent_message)
    }

    class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivedMessage = itemView.findViewById<TextView>(R.id.tv_received_message)
        val profileImage: ImageView = itemView.findViewById(R.id.iv_profile_picture)
    }

}