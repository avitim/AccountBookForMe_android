package com.example.accountbookforme.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.accountbookforme.R
import com.example.accountbookforme.viewmodel.StoresViewModel

class StoresFragment : Fragment() {

    private lateinit var storesViewModel: StoresViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        storesViewModel =
            ViewModelProvider(this).get(StoresViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_filter_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_filter)
        storesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}