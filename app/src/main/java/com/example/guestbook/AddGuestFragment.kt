package com.example.guestbook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.guestbook.data.Guest
import com.example.guestbook.data.GuestDao
import com.example.guestbook.data.GuestRepo
import com.example.guestbook.databinding.AddGuestBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.add_guest.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddGuestFragment : Fragment() {
    private val addGuestScope= CoroutineScope(Dispatchers.IO)
    private lateinit var guestDao: GuestDao
    private lateinit var viewModel:MainsViewModel
    private lateinit var binding:AddGuestBinding
    private var imgPathData:String=""


    override fun onAttach(context: Context) {
        super.onAttach(context)
        guestDao= GuestRepo.getDatabase(context).guestDao()
        viewModel=MainActivity.viewModel
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<AddGuestBinding>(inflater,
            R.layout.add_guest,container,false)

        binding.addGuestImage.setOnClickListener{
            chooseImage()
        }

        binding.submitButton.setOnClickListener{

                val name=binding.newName.text.toString()
                val address=binding.newAddress.text.toString()
                val email=binding.newMail.text.toString()
                val phone:Int=binding.newPhone.text.toString().toIntOrNull()?:0
                val comment=binding.newComment.text.toString()

                if(name.isNotEmpty()&&address.isNotEmpty()&&email.isNotEmpty()
                        &&comment.isNotEmpty()){
                   try{
                    addGuestScope.launch {
                        guestDao.insert(Guest(name,address,phone,email,comment,imgPathData))

                        this@AddGuestFragment.activity?.application
                                ?.let { it1 -> viewModel.updateGuests(it1) }
                    }}
                       catch(e:Exception){Snackbar.make(it,"Failed to insert user '$name'.",MainActivity.DELAY).show()}
                    findNavController().popBackStack()
                    Snackbar.make(it,"User $name successfully added.",MainActivity.DELAY).show()
                }else{
                Snackbar.make(it,"Please fill in all the text fields to proceed.",MainActivity.DELAY).show() }
        }
        return binding.root
    }

    private fun chooseImage(){
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,1)
    }

    fun uriToAbs(uri:Uri):String?{
        val filePath: String?
        val _uri = uri
        Log.d("", "URI = $_uri")
        val cursor: Cursor? = this.context?.contentResolver
                ?.query(_uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
        cursor?.moveToFirst()
        filePath = cursor?.getString(0)
        cursor?.close()
        return  filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            Glide.with(this).load(data?.data)
                    .transform(CenterCrop(), RoundedCorners(14))
                    .into(add_guest_image)

            imgPathData=data?.data.toString()
        }
    }
}