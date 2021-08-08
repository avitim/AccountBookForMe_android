package com.example.accountbookforme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PaymentsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is payments Fragment"
    }
    val text: LiveData<String> = _text
}