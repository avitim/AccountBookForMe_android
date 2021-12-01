package com.example.accountbookforme.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.accountbookforme.R
import com.example.accountbookforme.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Categoriesをタップしたらカテゴリ一覧画面に遷移
        binding.labelCategories.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_settings_to_settingCategoryFragment)
        }
        // Paymentsをタップしたら決済方法一覧画面に遷移
        binding.labelPayments.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_settings_to_settingPaymentFragment)
        }
        // Storesをタップしたら店舗一覧画面に遷移
        binding.labelStores.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_settings_to_settingStoreFragment)
        }
        // Accountをタップしたら店舗一覧画面に遷移
        binding.labelAccount.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_settings_to_settingAccountFragment)
        }

        return binding.root
    }
}