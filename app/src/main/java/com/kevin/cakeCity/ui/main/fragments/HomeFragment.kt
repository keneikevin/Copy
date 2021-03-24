package com.kevin.cakeCity.ui.main.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevin.cakeCity.R
import com.kevin.cakeCity.adapters.CakeAdapter
import com.kevin.cakeCity.databinding.FragmentHomeBinding
import com.kevin.cakeCity.ui.main.viewmodels.ShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment :Fragment(R.layout.fragment_home){
    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel : ShoppingViewModel
    lateinit var cakeAdapter: CakeAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        binding = FragmentHomeBinding.bind(view)
        setUpRecyclerView()
        lifecycleScope.launch {
         viewModel.getPagingFlow().collect {
            cakeAdapter.submitData(it)
         }
            }


    }
    private fun setUpRecyclerView() = binding.cakes.apply {
        cakeAdapter = CakeAdapter()
        adapter = cakeAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}













