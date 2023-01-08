package com.example.cinnamon.items

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cinnamon.IOnMainViewItemSelect
import com.example.cinnamon.database.RecipeItemRepository
import com.example.cinnamon.databinding.RecipeCellBinding
import kotlinx.coroutines.launch

@Entity(tableName = "main_table")
class RecipeItem (
	@ColumnInfo(name = "name") var name: String,
	@ColumnInfo(name = "image") var imageByteArray: ByteArray,
	@ColumnInfo(name = "time") var time: String,
	@ColumnInfo(name = "ingredients_names") var ingredientNames: String,
	@ColumnInfo(name = "ingredients_amounts") var ingredientAmounts: String,
	@ColumnInfo(name = "step_descriptions") var stepDescriptions: String,
	@ColumnInfo(name = "step_times") var stepTimes: String,
	@PrimaryKey(autoGenerate = true) var id: Int = 0
) {
	fun getImage(): Drawable = BitmapDrawable(BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size))

	fun getIngredients(): List<IngredientItem> = if (!ingredientNames.contains("<&S:>")) listOf()
		else {
			val ingNames = ingredientNames.split("<&S:>")
			val ingAmounts = ingredientAmounts.split("<&S:>")
			val ingredients: MutableList<IngredientItem> = mutableListOf()
			for (i in ingNames.indices) ingredients.add(IngredientItem(ingNames[i], ingAmounts[i]))
			ingredients.toList()
		}

	fun getSteps(): List<StepItem> = if (!ingredientNames.contains("<&S:>")) listOf()
		else {
			val stepDescriptions = stepDescriptions.split("<&S:>")
			val stepTimes = stepTimes.split("<&S:>")
			val steps: MutableList<StepItem> = mutableListOf()
			for (i in stepTimes.indices) steps.add(StepItem(stepDescriptions[i], stepTimes[i]))
			steps.toList()
		}
}

class RecipeItemAdapter(
	private val mainItems: List<RecipeItem>,
	private val listener: IOnMainViewItemSelect
): RecyclerView.Adapter<RecipeItemViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeItemViewHolder {
		val from = LayoutInflater.from(parent.context)
		val binding = RecipeCellBinding.inflate(from, parent, false)
		return RecipeItemViewHolder(binding, listener)
	}

	override fun onBindViewHolder(holder: RecipeItemViewHolder, position: Int) {
		holder.bindViewItem(mainItems[position])
	}

	override fun getItemCount(): Int = mainItems.size
}

class RecipeItemViewHolder(
	private val binding: RecipeCellBinding,
	private val listener: IOnMainViewItemSelect
) : RecyclerView.ViewHolder(binding.root) {
	fun bindViewItem(viewItem: RecipeItem) {
		binding.itemName.text = viewItem.name
		binding.itemImage.setImageDrawable(viewItem.getImage())

		binding.cellContainer.setOnClickListener {
			listener.showViewItem(viewItem)
		}
	}
}

class RecipeViewModel(private val repository: RecipeItemRepository): ViewModel() {
	var viewItems: LiveData<List<RecipeItem>> = repository.allItems.asLiveData()

	fun addItem(newItem: RecipeItem) = viewModelScope.launch { repository.insertItem(newItem) }

	fun updateItem(recipeItem: RecipeItem) = viewModelScope.launch { repository.updateItem(recipeItem) }

	fun deleteItem(recipeItem: RecipeItem) = viewModelScope.launch { repository.deleteItem(recipeItem) }
}

@Suppress("UNCHECKED_CAST")
class RecipeViewModelFactory(private val repository: RecipeItemRepository): ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(RecipeViewModel::class.java))
			return RecipeViewModel(repository) as T

		throw java.lang.IllegalArgumentException()
	}
}