package com.example.accountbookforme.ui.stores

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.accountbookforme.R

class StoresFragment : Fragment() {

    private lateinit var storesViewModel: StoresViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        storesViewModel =
            ViewModelProvider(this).get(StoresViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_stores, container, false)
        val textView: TextView = root.findViewById(R.id.text_stores)
        storesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}