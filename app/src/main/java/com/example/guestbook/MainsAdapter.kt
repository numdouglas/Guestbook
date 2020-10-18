package com.example.guestbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.guestbook.data.Guest
import kotlinx.android.synthetic.main.list_item.view.*

class MainsAdapter(private val guestData: MutableList<Guest>) : RecyclerView.Adapter<HomeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
                LayoutInflater.from(
                        parent.context
                ).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return guestData.size
    }


    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = guestData[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it)
                    .navigate(HomeScreenFragmentDirections
                            .actionHomeScreenFragmentToGuestDetailFragment
                            (item.name, item.address, item.phone, item.email, item.comment, item.picture))
        }
    }

    fun insertItem(newList: List<Guest>) {
        val diffUtilCB = DifferCalls(guestData, newList)
        val diffRS = DiffUtil.calculateDiff(diffUtilCB)

        guestData.addAll(newList)
        diffRS.dispatchUpdatesTo(this)

    }

    fun updateItem(newList: List<Guest>) {
        val diffUtilCB = DifferCalls(guestData, newList)
        val diffRS = DiffUtil.calculateDiff(diffUtilCB)

        guestData.clear()
        guestData.addAll(newList)
        diffRS.dispatchUpdatesTo(this)
    }

}

class DifferCalls(private val oldList: List<Guest>, private val newList: List<Guest>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemPosition == newItemPosition
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].phone == newList[newItemPosition].phone
    }
}


class HomeViewHolder(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val mName: TextView = itemView.re_name
    private val mPhone: TextView = itemView.re_phone
    private val mImage: ImageView = itemView.re_image


    fun bind(guest: Guest) {
        mName.text = guest.name
        mPhone.text = guest.phone.toString()
        Glide.with(itemView).load(guest.picture.toUri())
                .transform(CenterCrop(), RoundedCorners(14)).into(mImage)

    }
}
