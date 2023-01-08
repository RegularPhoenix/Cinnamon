package com.example.cinnamon.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.cinnamon.IOnStepViewItemSelect
import com.example.cinnamon.databinding.StepCellBinding
import java.util.UUID

class StepItemAdapter(
	private val items: List<StepItem>,
	private val listener: IOnStepViewItemSelect
): RecyclerView.Adapter<StepItemViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepItemViewHolder {
		val from = LayoutInflater.from(parent.context)
		val binding = StepCellBinding.inflate(from, parent, false)
		return StepItemViewHolder(binding, listener)
	}

	override fun onBindViewHolder(holder: StepItemViewHolder, position: Int) {
		holder.bindViewItem(items[position])
	}

	override fun getItemCount(): Int = items.size
}

class StepItemViewHolder(
	private val binding: StepCellBinding,
	private val listener: IOnStepViewItemSelect
) : RecyclerView.ViewHolder(binding.root) {
	fun bindViewItem(viewItem: StepItem) {
		binding.desc.text = viewItem.desc
		binding.time.text = viewItem.time

		binding.cellContainer.setOnClickListener {
			listener.showViewItem(viewItem)
		}
	}
}

class StepItemViewModel: ViewModel() {
	var viewItems = MutableLiveData<MutableList<StepItem>>()

	init { viewItems.value = mutableListOf() }

	fun addItem(newItem: StepItem) {
		val list = viewItems.value!!
		list.add(newItem)
		viewItems.postValue(list)
	}

	fun updateItem(id: UUID, desc: String, time: String) {
		val list = viewItems.value!!
		val item = list.find { it.id == id }!!
		item.desc = desc
		item.time = time
		viewItems.postValue(list)
	}
}

class StepItem (
	var desc: String,
	var time: String,
	var id: UUID = UUID.randomUUID()
)