package com.example.cinnamon.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinnamon.Cinnamon
import com.example.cinnamon.databinding.ActivityViewRecipeBinding
import com.example.cinnamon.databinding.IngredientCellBinding
import com.example.cinnamon.databinding.StepCellBinding
import com.example.cinnamon.fragments.EditRecipe
import com.example.cinnamon.items.*
import java.util.*

class ViewRecipeActivity: AppCompatActivity() {
	private lateinit var binding: ActivityViewRecipeBinding
	private val recipeViewModel: RecipeViewModel by viewModels { RecipeViewModelFactory((application as Cinnamon).repository) }
	private lateinit var showIngredientViewModel: ShowIngredientViewModel
	private lateinit var showStepViewModel: ShowStepViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		showIngredientViewModel = ViewModelProvider(this)[ShowIngredientViewModel::class.java]
		showStepViewModel = ViewModelProvider(this)[ShowStepViewModel::class.java]

		binding = ActivityViewRecipeBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val bundle = intent.extras!!
		val id = bundle.getInt("id")

		var recipeItem: RecipeItem? = null

		recipeViewModel.viewItems.observe(this) { IT ->
			try {
				recipeItem = IT.first { it.id == id }
				binding.title.apply { text = recipeItem!!.name }
				binding.image.apply { setImageDrawable(recipeItem!!.getImage()) }

				for (item in recipeItem!!.getIngredients()) {
					showIngredientViewModel.addItem(item)
				}

				for (item in recipeItem!!.getSteps()) {
					showStepViewModel.addItem(item)
				}
			}
			catch (e: java.lang.Exception) { finish() }
		}

		binding.editButton.setOnClickListener {
			EditRecipe(recipeItem).show(supportFragmentManager, "editViewTag")
		}

		setViews()
	}

	private fun setViews() {
		showIngredientViewModel.viewItems.observe(this) {
			binding.ingView.apply {
				layoutManager = LinearLayoutManager(context)
				adapter = ShowIngredientViewItemAdapter(it)
			}
		}
		showStepViewModel.viewItems.observe(this) {
			binding.stepView.apply {
				layoutManager = LinearLayoutManager(context)
				adapter = ShowStepViewItemAdapter(it)
			}
		}
	}
}

class ShowIngredientItemViewHolder(
	private val binding: IngredientCellBinding
) : RecyclerView.ViewHolder(binding.root) {
	fun bindViewItem(viewItem: IngredientItem) {
		binding.Name.text = viewItem.name
		binding.Amount.text = viewItem.amount
	}
}

class ShowIngredientViewModel: ViewModel() {
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

class ShowIngredientViewItemAdapter(
	private val items: List<IngredientItem>
): RecyclerView.Adapter<ShowIngredientItemViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowIngredientItemViewHolder {
		val from = LayoutInflater.from(parent.context)
		val binding = IngredientCellBinding.inflate(from, parent, false)
		return ShowIngredientItemViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ShowIngredientItemViewHolder, position: Int) {
		holder.bindViewItem(items[position])
	}

	override fun getItemCount(): Int = items.size
}

class ShowStepItemViewHolder(
	private val binding: StepCellBinding
) : RecyclerView.ViewHolder(binding.root) {
	fun bindViewItem(viewItem: StepItem) {
		binding.desc.text = viewItem.desc
		binding.time.text = viewItem.time
	}
}

class ShowStepViewModel: ViewModel() {
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

class ShowStepViewItemAdapter(
	private val items: List<StepItem>
): RecyclerView.Adapter<ShowStepItemViewHolder>() {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowStepItemViewHolder {
		val from = LayoutInflater.from(parent.context)
		val binding = StepCellBinding.inflate(from, parent, false)
		return ShowStepItemViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ShowStepItemViewHolder, position: Int) {
		holder.bindViewItem(items[position])
	}

	override fun getItemCount(): Int = items.size
}