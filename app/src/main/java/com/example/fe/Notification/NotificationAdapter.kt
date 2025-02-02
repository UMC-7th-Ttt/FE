package com.example.fe.Notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R

class NotificationAdapter(private val notifications: List<AppNotification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.notification_icon)
        val title: TextView = itemView.findViewById(R.id.notification_title)
        val description: TextView = itemView.findViewById(R.id.notification_description)
        val time: TextView = itemView.findViewById(R.id.notification_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.icon.setImageResource(notification.iconRes)
        holder.title.text = notification.title
        holder.description.text = notification.description
        holder.time.text = notification.time
    }

    override fun getItemCount(): Int = notifications.size
}