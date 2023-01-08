package com.example.cinnamon.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.cinnamon.databinding.FragmentEditIngredientBinding
import com.example.cinnamon.items.IngredientItem
import com.example.cinnamon.items.IngredientItemViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewIngredient(private var item: IngredientItem?) : BottomSheetDialogFragment() {
	private lateinit var binding: FragmentEditIngredientBinding
	private lateinit var model: IngredientItemViewModel

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val activity = requireActivity()
		model = ViewModelProvider(activity)[IngredientItemViewModel::class.java]

		if (item != null) {
			val editable = Editable.Factory.getInstance()
			binding.ingName.text = editable.newEditable(item!!.name)
			binding.ingAmount.text = editable.newEditable(item!!.amount)
		}

		binding.saveIngButton.setOnClickListener {
			saveAction()
		}
	}

	private fun saveAction() {
		val name = binding.ingName.text.toString()
		val amount = binding.ingAmount.text.toString()
		if (item == null) {
			val newItem = IngredientItem(name, amount)
			model.addItem(newItem)
		}
		else {
			model.updateItem(item!!.id, name, amount)
		}
		binding.ingName.setText("")
		binding.ingAmount.setText("")
		dismiss()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
		binding = FragmentEditIngredientBinding.inflate(inflater, container, false)
		return binding.root
	}

}