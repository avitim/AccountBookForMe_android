package com.example.accountbookforme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.accountbookforme.R
import com.example.accountbookforme.model.Filter

class DialogExpenseStoreAdapter(context: Context, private var storeList: List<Filter>) :
    ArrayAdapter<Filter>(context, 0, storeList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater = LayoutInflater.from(parent.context)

        val store = storeList[position]

        var view = convertView

        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_filter_item, parent, false)
        }

        val idView = view?.findViewById<TextView>(R.id.dialog_filter_id)
        idView?.text = store.id.toString()

        val nameView = view?.findViewById<TextView>(R.id.dialog_filter_name)
        nameView?.text = store.name

        return view!!
    }
}