package com.example.guestbook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.finishAfterTransition
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guestbook.data.Guest
import com.example.guestbook.data.GuestDao
import com.example.guestbook.data.GuestRepo
import com.example.guestbook.databinding.HomeScreenBinding
import kotlinx.android.synthetic.main.home_screen.recycler
import androidx.core.app.ActivityCompat.recreate

class HomeScreenFragment : Fragment() {
    private lateinit var binding: HomeScreenBinding
    private lateinit var mainsAdapter: MainsAdapter
    private lateinit var viewModel: MainsViewModel

    private lateinit var guestDao: GuestDao

    override fun onAttach(context: Context) {
        super.onAttach(context)
        guestDao = GuestRepo.getDatabase(context).guestDao()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        viewModel = MainActivity.viewModel
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<HomeScreenBinding>(inflater,
                R.layout.home_screen, container, false)

        binding.addNewButton.setOnClickListener {

            findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToAddGuestFragment())
        }
        viewModel.guests?.observe(viewLifecycleOwner, { guestList ->
            mainsAdapter.insertItem(guestList)
            mainsAdapter.updateItem(guestList)

        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recycler.adapter = null
    }


    private fun initRecyclerView() {
        val initVals: MutableList<Guest> = arrayListOf()
        mainsAdapter = MainsAdapter(initVals)

        recycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mainsAdapter
        }
    }
}

