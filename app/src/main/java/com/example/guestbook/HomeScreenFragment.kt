package com.example.guestbook

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guestbook.data.GuestDao
import com.example.guestbook.data.GuestRepo
import com.example.guestbook.databinding.HomeScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class HomeScreenFragment : Fragment() {
    private lateinit var binding:HomeScreenBinding
    private lateinit var mainsAdapter: MainsAdapter
    private lateinit var viewModel: MainsViewModel

    private lateinit var guestDao: GuestDao


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
        binding = DataBindingUtil.inflate<HomeScreenBinding>(inflater,
            R.layout.home_screen,container,false)

        binding.addNewButton.setOnClickListener {

            findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToAddGuestFragment())
        }
        viewModel.guests?.observe(viewLifecycleOwner, {
            mainsAdapter.submitList(it) })


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }


    private fun initRecyclerView() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(activity)
            mainsAdapter = MainsAdapter()
            adapter = mainsAdapter
        }
    }
}

