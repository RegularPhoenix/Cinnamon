package com.example.cinnamon.fragments

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinnamon.IOnIngredientViewItemSelect
import com.example.cinnamon.IOnStepViewItemSelect
import com.example.cinnamon.R
import com.example.cinnamon.databinding.FragmentEditRecipeBinding
import com.example.cinnamon.items.*
import java.io.ByteArrayOutputStream


class EditRecipe(private var mainItem: RecipeItem?) : DialogFragment(), IOnIngredientViewItemSelect,
	IOnStepViewItemSelect {
	private lateinit var binding: FragmentEditRecipeBinding
	private lateinit var recipeViewModel: RecipeViewModel
	private lateinit var ingredientItemViewModel: IngredientItemViewModel
	private lateinit var stepItemViewModel: StepItemViewModel

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val activity = requireActivity()

		recipeViewModel = ViewModelProvider(activity)[RecipeViewModel::class.java]
		ingredientItemViewModel = ViewModelProvider(activity)[IngredientItemViewModel::class.java]
		stepItemViewModel = ViewModelProvider(activity)[StepItemViewModel::class.java]

		if (mainItem != null) {
			binding.itemName.text = Editable.Factory.getInstance().newEditable(mainItem!!.name)
			binding.itemImage.setImageDrawable(mainItem!!.getImage())
			binding.deleteButton.text = getString(R.string.delete_string)
			for (item in mainItem!!.getIngredients()) { ingredientItemViewModel.addItem(item) }
			for (item in mainItem!!.getSteps()) { stepItemViewModel.addItem(item) }
		}
		else
			binding.deleteButton.text = getString(R.string.dismiss_string)

		binding.itemImage.setOnClickListener {
			val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
			startActivityForResult(gallery, 100)
		}
		binding.saveButton.setOnClickListener { saveAction() }
		binding.deleteButton.setOnClickListener { deleteAction() }
		binding.addIngredientButton.setOnClickListener { NewIngredient(null).show(activity.supportFragmentManager, "newViewTag") }
		binding.addStepButton.setOnClickListener { NewStep(null).show(activity.supportFragmentManager, "newViewTag") }

		setListViews()
	}

	private fun setListViews() {
		val activity = this
		ingredientItemViewModel.viewItems.observe(this) {
			binding.ingredientsList.apply {
				layoutManager = LinearLayoutManager(context)
				adapter = IngredientItemAdapter(it, activity)
			}
		}
		stepItemViewModel.viewItems.observe(this) {
			binding.stepsList.apply {
				layoutManager = LinearLayoutManager(context)
				adapter = StepItemAdapter(it, activity)
			}
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == RESULT_OK && requestCode == 100) {
			val uri = data?.data
			val inputStream = uri?.let { requireActivity().contentResolver.openInputStream(it) }
			val drawable = Drawable.createFromStream(inputStream, uri.toString())
			binding.itemImage.setImageDrawable(drawable)
		}
	}

	private fun deleteAction() {
		if (mainItem != null)
			recipeViewModel.deleteItem(mainItem!!)
		dismiss()
	}

	private fun saveAction() {
		val name = if (binding.itemName.text.toString() != "") binding.itemName.text.toString() else getString(
			R.string.empty_item_name_placeholder
		)
		val imageDrawable = binding.itemImage.drawable
		val byteArray = drawableToByteArray(imageDrawable)
		val ingNames: MutableList<String> = mutableListOf()
		val ingAmounts: MutableList<String> = mutableListOf()
		val stepDescriptions: MutableList<String> = mutableListOf()
		val stepTimes: MutableList<String> = mutableListOf()
		ingredientItemViewModel.viewItems.observe(this) {
			for (item in it) {
				ingNames.add(item.name)
				ingAmounts.add(item.amount)
			}
		}
		stepItemViewModel.viewItems.observe(this) {
			for (item in it) {
				stepDescriptions.add(item.desc)
				stepTimes.add(item.time)
			}
		}

		if (mainItem == null) {
			val newItem = RecipeItem(name, byteArray, "--:--", listToStr(ingNames), listToStr(ingAmounts), listToStr(stepDescriptions), listToStr(stepTimes))
			recipeViewModel.addItem(newItem)
		}
		else {
			mainItem!!.name = name
			mainItem!!.imageByteArray = byteArray
			mainItem!!.ingredientNames = listToStr(ingNames)
			mainItem!!.ingredientAmounts = listToStr(ingAmounts)
			mainItem!!.stepDescriptions = listToStr(stepDescriptions)
			mainItem!!.stepTimes = listToStr(stepTimes)
			recipeViewModel.updateItem(mainItem!!)
		}

		binding.itemName.setText("")
		binding.itemImage.setImageDrawable(ResourcesCompat.getDrawable(resources,
			R.drawable.ic_baseline_cookie_24, null))
		ingredientItemViewModel.viewItems.observe(this) { it.clear() }
		stepItemViewModel.viewItems.observe(this) { it.clear() }
		dismiss()
	}

	override fun onStart() {
		super.onStart()
		val dialog: Dialog? = dialog
		if (dialog != null) {
			val width = ViewGroup.LayoutParams.MATCH_PARENT
			val height = ViewGroup.LayoutParams.MATCH_PARENT
			dialog.window!!.setLayout(width, height)
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
		binding = FragmentEditRecipeBinding.inflate(inflater, container, false)
		return binding.root
	}

	private fun drawableToByteArray(drawable: Drawable): ByteArray {
		val imageBitmap = drawable.toBitmap(256, 256)
		val stream = ByteArrayOutputStream()
		imageBitmap.compress(Bitmap.CompressFormat.PNG,100, stream)
		return stream.toByteArray()
	}

	private fun listToStr(list: List<String>): String = list.joinToString("<&S:>")


	override fun showViewItem(ingredientItem: IngredientItem) {
		NewIngredient(ingredientItem).show(requireActivity().supportFragmentManager, "editViewTag")
	}

	override fun showViewItem(stepItem: StepItem) {
		NewStep(stepItem).show(requireActivity().supportFragmentManager, "editViewTag")
	}
}