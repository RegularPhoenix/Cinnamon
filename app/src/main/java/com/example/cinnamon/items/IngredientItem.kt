package com.example.cinnamon.items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.cinnamon.IOnIngredientViewItemSelect
import com.example.cinnamon.databinding.IngredientCellBinding
import java.util.UUID

class IngredientItemAdapter(
	private val items: List<IngredientItem>,
	private val listener: IOnIngredientViewItemSelect
): RecyclerView.Adapter<IngredientItemViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientItemViewHolder {
		val from = LayoutInflater.from(parent.context)
		val binding = IngredientCellBinding.inflate(from, parent, false)
		return IngredientItemViewHolder(binding, listener)
	}

	override fun onBindViewHolder(holder: IngredientItemViewHolder, position: Int) {
		holder.bindViewItem(items[position])
	}

	override fun getItemCount(): Int = items.size
}

class IngredientItemViewHolder(
	private val binding: IngredientCellBinding,
	private val listener: IOnIngredientViewItemSelect
) : RecyclerView.ViewHolder(binding.root) {
	fun bindViewItem(viewItem: IngredientItem) {
		binding.Name.text = viewItem.name
		binding.Amount.text = viewItem.amount

		binding.cellContainer.setOnClickListener {
			listener.showViewItem(viewItem)
		}
	}
}

class IngredientItemViewModel: ViewModel() {
	var viewItems = MutableLiveData<MutableList<IngredientItem>>()

	init { viewItems.value = mutableListOf() }

	fun addItem(newItem: IngredientItem) {
		val list = viewItems.value!!
		list.add(newItem)
		viewItems.postValue(list)
	}

	fun updateItem(id: UUID, name: String, amount: String) {
		val list = viewItems.value!!
		val item = list.find { it.id == id }!!
		item.name = name
		item.amount = amount
		viewItems.postValue(list)
	}
}

class IngredientItem (
	var name: String,
	var amount: String,
	var id: UUID = UUID.randomUUID()
)