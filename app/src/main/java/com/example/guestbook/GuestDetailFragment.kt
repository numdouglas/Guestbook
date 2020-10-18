package com.example.guestbook

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.guestbook.data.GuestDao
import com.example.guestbook.data.GuestRepo
import com.example.guestbook.databinding.GuestDetailBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuestDetailFragment : Fragment() {
    private val guestDetailScope = CoroutineScope(Dispatchers.IO)
    private lateinit var guestDao: GuestDao
    private lateinit var viewModel: MainsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        guestDao = GuestRepo.getDatabase(context).guestDao()
        viewModel = MainActivity.viewModel
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val argumentsGiven = arguments?.let { GuestDetailFragmentArgs.fromBundle(it) }

        val binding = DataBindingUtil.inflate<GuestDetailBinding>(inflater,
                R.layout.guest_detail, container, false)

        binding.name.text = argumentsGiven?.name
        binding.addressDetail.text = argumentsGiven?.address
        binding.phoneDetail.text = argumentsGiven?.phone.toString()
        binding.emailDetail.text = argumentsGiven?.email
        binding.commentDetail.text = argumentsGiven?.comment
        Glide.with(this).load(argumentsGiven?.imgUrl?.toUri())
                .transform(CenterCrop(), RoundedCorners(2))
                .into(binding.pictureDetail)



        binding.deleteButton.setOnClickListener {
            try {
                guestDetailScope.launch {

                    guestDao.deleteByPhoneNumber(argumentsGiven!!.phone)

                    this@GuestDetailFragment.activity?.application?.let { it1 -> viewModel.updateGuests(it1) }
                }
                Snackbar.make(it, "User '${argumentsGiven?.name}' successfully deleted.", MainActivity.DELAY).show()
            } catch (e: Exception) {
                Snackbar.make(it, "Error deleting the user '${argumentsGiven?.name}'.", MainActivity.DELAY).show()
            }
            findNavController().popBackStack()
        }


        return binding.root
    }
}