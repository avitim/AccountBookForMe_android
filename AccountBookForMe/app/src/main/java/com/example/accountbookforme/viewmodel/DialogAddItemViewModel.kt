package com.example.accountbookforme.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.accountbookforme.model.Item

class DialogAddItemViewModel : ViewModel() {

    var item: MutableLiveData<Item> = MutableLiveData()
}