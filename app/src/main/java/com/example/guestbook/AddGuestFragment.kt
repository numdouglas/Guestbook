package com.example.guestbook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.guestbook.data.Guest
import com.example.guestbook.data.GuestDao
import com.example.guestbook.data.GuestRepo
import com.example.guestbook.databinding.AddGuestBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddGuestFragment : Fragment() {
    private val addGuestScope= CoroutineScope(Dispatchers.IO)
    private lateinit var guestDao: GuestDao
    private lateinit var viewModel: MainsViewModel
    private lateinit var binding:AddGuestBinding
    private var imgPathData:Uri?=null


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
        binding = DataBindingUtil.inflate<AddGuestBinding>(inflater,
            R.layout.add_guest,container,false)

        binding.imageView.setOnClickListener{
            chooseImage()
        }

        binding.submitButton.setOnClickListener{
            addGuestScope.launch {
                val name=binding.name.text.toString()
                val address=binding.address.text.toString()
                val email=binding.email.text.toString()
                val phone=binding.phone.text.toString().toInt()
                val comment=binding.comment.text.toString()

                guestDao.insertAll(Guest(name,address,phone,email,comment,""))

                val allguests=guestDao.getAll()
                viewModel.guests?.postValue(allguests)
            }

            findNavController().popBackStack()

        }
        return binding.root
    }

    private fun chooseImage(){
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            binding.imageView.setImageURI(data?.data)
            imgPathData=data?.data
        }
    }
}