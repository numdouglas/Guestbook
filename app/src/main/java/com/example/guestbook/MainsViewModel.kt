package com.example.guestbook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.guestbook.data.Guest
import com.example.guestbook.data.GuestRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainsViewModel (application: Application) : AndroidViewModel(application)
{
    var guests:MutableLiveData<List<Guest>>?= MutableLiveData()
    
    init {
        updateGuests(application)
    }

    fun updateGuests(application: Application){
      viewModelScope.launch(Dispatchers.IO) {
          val guestDao= GuestRepo.getDatabase(application.applicationContext).guestDao()

          guests?.postValue(guestDao.getAll()) }
    }
}
