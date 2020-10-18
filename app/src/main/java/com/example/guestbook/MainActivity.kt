package com.example.guestbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karumi.dexter.Dexter
import android.Manifest
import androidx.lifecycle.ViewModelProvider
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var viewModel: MainsViewModel
        val DELAY=4000
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        viewModel =
            ViewModelProvider
                    .AndroidViewModelFactory
                    .getInstance(this.application)
                    .create(MainsViewModel::class.java)

        val listener:PermissionListener=DialogOnDeniedPermissionListener.Builder
                .withContext(this)
                .withTitle("Read Files Permission")
                .withMessage("Read External Storage Permission Needed")
                .withButtonText(android.R.string.ok)
                .build()
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(listener)
                .onSameThread()
                .check()

        setContentView(R.layout.activity_main)



    }


}

