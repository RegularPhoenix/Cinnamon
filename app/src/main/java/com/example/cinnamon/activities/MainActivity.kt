package com.example.cinnamon.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinnamon.Cinnamon
import com.example.cinnamon.IOnMainViewItemSelect
import com.example.cinnamon.databinding.ActivityMainBinding
import com.example.cinnamon.fragments.EditRecipe
import com.example.cinnamon.items.RecipeItem
import com.example.cinnamon.items.RecipeItemAdapter
import com.example.cinnamon.items.RecipeViewModel
import com.example.cinnamon.items.RecipeViewModelFactory

class MainActivity : AppCompatActivity(), IOnMainViewItemSelect {
    private lateinit var binding: ActivityMainBinding
    private val recipeViewModel: RecipeViewModel by viewModels { RecipeViewModelFactory((application as Cinnamon).repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.newViewElementButton.setOnClickListener {
            EditRecipe(null).show(supportFragmentManager, "newViewTag")
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val mainActivity = this
        recipeViewModel.viewItems.observe(this) {
            binding.mainRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = RecipeItemAdapter(it, mainActivity)
            }
        }
    }

    override fun showViewItem(recipeItem: RecipeItem) {
        val intent = Intent(this, ViewRecipeActivity::class.java)
        intent.putExtra("id", recipeItem.id)
        startActivity(intent)
    }
}