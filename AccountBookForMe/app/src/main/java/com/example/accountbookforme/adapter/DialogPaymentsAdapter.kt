package com.example.accountbookforme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.accountbookforme.R
import com.example.accountbookforme.model.Payment

class DialogPaymentsAdapter(context: Context, var paymentList: List<Payment>) :
    ArrayAdapter<Payment>(context, 0, paymentList) {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val payment = paymentList[position]

        var view = convertView

        if (view == null) {
            view = layoutInflater.inflate(R.layout.filter_list_item, parent, false)
        }

        val idView = view?.findViewById<TextView>(R.id.dialog_filter_id)
        idView?.text = payment.id.toString()

        val nameView = view?.findViewById<TextView>(R.id.dialog_filter_name)
        nameView?.text = payment.id.toString()

        return view!!
    }
}