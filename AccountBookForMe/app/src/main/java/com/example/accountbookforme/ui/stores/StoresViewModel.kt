package com.example.accountbookforme.ui.stores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StoresViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is stores Fragment"
    }
    val text: LiveData<String> = _text
}