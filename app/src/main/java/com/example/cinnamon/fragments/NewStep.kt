package com.example.cinnamon.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.cinnamon.databinding.FragmentEditStepBinding
import com.example.cinnamon.items.StepItem
import com.example.cinnamon.items.StepItemViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewStep(private var item: StepItem?) : BottomSheetDialogFragment() {
	private lateinit var binding: FragmentEditStepBinding
	private lateinit var model: StepItemViewModel

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val activity = requireActivity()
		model = ViewModelProvider(activity)[StepItemViewModel::class.java]

		if (item != null) {
			val editable = Editable.Factory.getInstance()
			binding.stepDesc.text = editable.newEditable(item!!.desc)
			binding.stepTime.text = editable.newEditable(item!!.time)
		}

		binding.saveIngButton.setOnClickListener {
			saveAction()
		}
	}

	private fun saveAction() {
		val desc = binding.stepDesc.text.toString()
		val time = binding.stepTime.text.toString()
		if (item == null) {
			val newItem = StepItem(desc, time)
			model.addItem(newItem)
		}
		else {
			model.updateItem(item!!.id, desc, time)
		}
		binding.stepDesc.setText("")
		binding.stepTime.setText("")
		dismiss()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
		binding = FragmentEditStepBinding.inflate(inflater, container, false)
		return binding.root
	}

}