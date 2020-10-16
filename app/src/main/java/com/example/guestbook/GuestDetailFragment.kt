package com.example.guestbook

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.guestbook.data.GuestDao
import com.example.guestbook.data.GuestRepo
import com.example.guestbook.databinding.GuestDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuestDetailFragment:Fragment() {
    private val guestDetailScope= CoroutineScope(Dispatchers.IO)
    private lateinit var guestDao: GuestDao
    private lateinit var viewModel: MainsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        guestDao= GuestRepo.getDatabase(context).guestDao()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        viewModel = ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(activity.application)
                .create(MainsViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val argumentsGiven= arguments?.let { GuestDetailFragmentArgs.fromBundle(it) }

        val binding = DataBindingUtil.inflate<GuestDetailBinding>(inflater,
                R.layout.guest_detail,container,false)

        binding.name.text=argumentsGiven?.name
        binding.addressDetail.text=argumentsGiven?.address
        binding.phoneDetail.text=argumentsGiven?.phone.toString()
        binding.emailDetail.text=argumentsGiven?.email
        binding.commentDetail.text=argumentsGiven?.comment



        binding.deleteButton.setOnClickListener {
            guestDetailScope.launch {
                guestDao.deleteByPhoneNumber(argumentsGiven!!.phone)
                val allguests=guestDao.getAll()
                viewModel.guests?.postValue(allguests)}

            Toast.makeText(it.context,"User ${argumentsGiven?.name} successfully deleted",Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }


        return binding.root}
}