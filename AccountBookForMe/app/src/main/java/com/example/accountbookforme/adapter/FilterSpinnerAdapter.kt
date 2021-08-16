package com.example.accountbookforme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.accountbookforme.R
import com.example.accountbookforme.model.Filter

class FilterSpinnerAdapter(context: Context, private val filterList: List<Filter>) :
    ArrayAdapter<Filter>(context, 0, filterList) {

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView?: inflater.inflate(R.layout.list_filter_item, parent, false)
        view.findViewById<TextView>(R.id.dialog_filter_name).text = getItem(position).name

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView?: inflater.inflate(R.layout.list_filter_item, parent, false)
        view.findViewById<TextView>(R.id.dialog_filter_name).text = getItem(position).name

        return view

    }

    override fun getItem(position: Int) = filterList[position]

}