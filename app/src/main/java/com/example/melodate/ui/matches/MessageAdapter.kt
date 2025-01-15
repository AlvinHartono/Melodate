package com.example.melodate.ui.matches

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
            // check if the message has "https://storage.googleapis.com/"
            if (currentMessage.message!!.startsWith("https://storage.googleapis.com/")) {
                //make the image view visible
                viewHolder.sentMessage.visibility = View.GONE
                viewHolder.sentImage.visibility = View.VISIBLE

                Glide.with(context).load(currentMessage.message)
                    .transform(CenterCrop(), RoundedCorners(120))
                    .placeholder(R.drawable.baseline_person_24)
                    .into(holder.itemView.findViewById(R.id.iv_sent_image))

                // handle onclick listener for the image view
                holder.itemView.findViewById<ImageView>(R.id.iv_sent_image).setOnClickListener {
                    val intent = Intent(context, ImageActivity::class.java)
                    intent.putExtra("imageUrl", currentMessage.message)
                    context.startActivity(intent)
                }
            } else {
                //make the image view gone
                viewHolder.sentMessage.visibility = View.VISIBLE
                viewHolder.sentImage.visibility = View.GONE
                holder.sentMessage.text = currentMessage.message
            }


        } else {
            //do the stuff for received view holder
            // check if the message has "https://storage.googleapis.com/"
            val viewHolder = holder as ReceivedViewHolder
            Glide.with(context).load(currentMessage.profileUrl)
                .placeholder(R.drawable.baseline_person_24).circleCrop().into(holder.profileImage)

            if (currentMessage.message!!.startsWith("https://storage.googleapis.com/")) {
                //make the image view visible
                holder.itemView.findViewById<ImageView>(R.id.iv_received_image).visibility =
                    View.VISIBLE
                holder.itemView.findViewById<TextView>(R.id.tv_received_message).visibility =
                    View.GONE
                Glide.with(context).load(currentMessage.message)
                    .transform(CenterCrop(), RoundedCorners(120))
                    .placeholder(R.drawable.baseline_person_24)
                    .into(holder.itemView.findViewById(R.id.iv_received_image))
                // handle onclick listener for the image view
                holder.itemView.findViewById<ImageView>(R.id.iv_received_image).setOnClickListener {
                    val intent = Intent(context, ImageActivity::class.java)
                    intent.putExtra("imageUrl", currentMessage.message)
                    context.startActivity(intent)
                }
            } else {
                //make the image view gone
                viewHolder.receivedMessage.visibility = View.VISIBLE
                viewHolder.receivedImage.visibility = View.GONE
                holder.receivedMessage.text = currentMessage.message
            }


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
        val sentImage = itemView.findViewById<ImageView>(R.id.iv_sent_image)
    }

    class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivedMessage = itemView.findViewById<TextView>(R.id.tv_received_message)
        val profileImage: ImageView = itemView.findViewById(R.id.iv_profile_picture)
        val receivedImage = itemView.findViewById<ImageView>(R.id.iv_received_image)

    }

}