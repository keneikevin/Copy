package com.kevin.cakeCity.ui.main.fragments
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevin.cakeCity.R
import com.kevin.cakeCity.adapters.ShoppingAdapter
import com.kevin.cakeCity.databinding.FragmentShoppingBinding
import com.kevin.cakeCity.ui.main.viewmodels.ShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint

import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

@AndroidEntryPoint
class ShoppingFragment :Fragment(R.layout.fragment_shopping) {
    private lateinit var binding: FragmentShoppingBinding
    lateinit var viewModel: ShoppingViewModel
    private lateinit var shoppingAdapter: ShoppingAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        binding = FragmentShoppingBinding.bind(view)
        subscribeToObservers()
        setupRecyclerView()

        binding.fabCart.setOnClickListener {
           Toast.makeText(requireActivity(),"buy",Toast.LENGTH_LONG).show()
        }}


    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            0, LEFT or RIGHT
    ) {
        override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = shoppingAdapter.shoppingItems[pos]
            viewModel?.deleteShoppingItem(item)
            Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    viewModel?.insertShoppingItemIntoDb(item)
                }
                show()
            }
        }
    }
    private fun subscribeToObservers() {
        viewModel.totalPrice.observe(viewLifecycleOwner, Observer {
            val price = it ?: 0f
            val priceText = "Total: $price ksh"
            binding.tvCakePrice.text = priceText
        })
        viewModel.shoppingItems.observe(viewLifecycleOwner, Observer {
            shoppingAdapter.shoppingItems = it
        })

    }
    private fun setupRecyclerView() {
        binding.rvShoppingItems.apply {
            shoppingAdapter = ShoppingAdapter()
            adapter = shoppingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }
}
