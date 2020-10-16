package com.example.guestbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.guestbook.data.Guest
import kotlinx.android.synthetic.main.list_item.view.*

class MainsAdapter: ListAdapter<Guest, HomeViewHolder>(GuestsDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {

            Navigation.findNavController(it)
                    .navigate(HomeScreenFragmentDirections
                        .actionHomeScreenFragmentToGuestDetailFragment
                            (item.name,item.address,item.phone,item.email,item.comment)) }
}}

class GuestsDiffCallBack : DiffUtil.ItemCallback<Guest>() {
    override fun areItemsTheSame(oldItem: Guest, newItem: Guest): Boolean {
        return oldItem.phone == newItem.phone
    }

    override fun areContentsTheSame(oldItem: Guest, newItem: Guest): Boolean {
        return oldItem == newItem
    }

}



class HomeViewHolder constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val mName: TextView = itemView.re_name
    private val mPhone:TextView=itemView.re_phone


    fun bind(guest: Guest) {
        mName.text =guest.name
        mPhone.text=guest.phone.toString()

    }
}
